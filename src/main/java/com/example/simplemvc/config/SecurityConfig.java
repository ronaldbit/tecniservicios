package com.example.simplemvc.config;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.shared.config.Route;
import com.example.simplemvc.shared.filter.JwtRequestFilter;
import com.example.simplemvc.shared.properties.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
  private final SecurityProperties securityProperties;
  private final JwtRequestFilter jwtRequestFilter;
  private final ObjectMapper objectMapper;

  @Value("${spring.mvc.servlet.path}")
  private String basePath;

  @Bean
  @Order(1)
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(FormLoginConfigurer::disable)
        .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.NEVER))
        .authorizeHttpRequests(
            authRequest -> {
              // authRequest.requestMatchers(securityProperties.getPublicRoutes()).permitAll();
              /**
               * provideRoutes(securityProperties.getNoAdminOperationToSelfRoutes(), (method,
               * paths)
               * -> authRequest.requestMatchers(method, paths) .access((authentication,
               * object) ->
               * new AuthorizationDecision( isAdminAndNotSelf(authentication, object))));
               *
               * <p>
               * provideRoutes(securityProperties.getAdminRoutes(), (method, paths) ->
               * authRequest.requestMatchers(method, paths).hasRole("ADMIN"));
               */
              authRequest.anyRequest().permitAll();
            })
        .exceptionHandling(
            (exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(this::manageNoAuthorized)
                .accessDeniedHandler(this::manageNoAuthorized))
        .logout(
            logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler(
                    (request, response, authentication) -> {
                      if (request.getSession(false) != null)
                        request.getSession().invalidate();

                      response.addHeader(
                          "Set-Cookie",
                          "JWT_TOKEN=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax");
                      response.addHeader(
                          "Set-Cookie",
                          "JSESSIONID=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax");

                      response.sendRedirect("/auth/login");
                    }))
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  @Order(2)
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOrigins(securityProperties.getAllowedOrigins());
    config.setAllowedMethods(securityProperties.getAllowedMethods());
    config.setAllowedHeaders(securityProperties.getAllowedHeaders());
    config.setAllowCredentials(securityProperties.isAllowCredentials());

    for (String exposedHeader : securityProperties.getExposedHeaders()) {
      config.addExposedHeader(exposedHeader);
    }

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder;
  }

  private void manageNoAuthorized(
      HttpServletRequest request, HttpServletResponse response, RuntimeException e)
      throws IOException {

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.sendRedirect("/errors/403");
  }

  private void provideRoutes(Route[] routes, BiConsumer<HttpMethod, String[]> consumer) {
    if (routes == null) {
      log.warn("No se proveeron rutas");
      return;
    }

    for (Route route : routes) {
      var rawMethods = route.getMethods();
      HttpMethod[] methods = new HttpMethod[rawMethods.length];

      for (int i = 0; i < rawMethods.length; i++) {
        methods[i] = HttpMethod.valueOf(rawMethods[i].toString());
      }

      for (HttpMethod method : methods) {
        consumer.accept(method, route.getRoutes());
      }

      if (!Stream.of(methods).anyMatch(method -> method.equals(HttpMethod.OPTIONS)))
        consumer.accept(HttpMethod.OPTIONS, route.getRoutes());
    }
  }

  private boolean isAdminAndNotSelf(
      Supplier<Authentication> authentication, RequestAuthorizationContext context) {
    Authentication auth = authentication.get();

    if (auth == null) {
      return false;
    }

    if (!(auth.getPrincipal() instanceof Usuario)) {
      return false;
    }

    Usuario usuario = (Usuario) auth.getPrincipal();

    // if (!usuario.getAuthorities().contains(UsuarioRol.ADMIN)) {
    // return false;
    // }

    String id = context.getVariables().get("id").toString();

    if (id == null) {
      log.warn("El id no ha sido proveído: {}", context.getRequest().getRequestURI());
      return false;
    }

    return !id.equals(usuario.getId().toString());
  }
}
