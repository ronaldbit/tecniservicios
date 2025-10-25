package com.example.simplemvc.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.model.UsuarioRol;
import com.example.simplemvc.shared.config.Route;
import com.example.simplemvc.shared.filter.JwtRequestFilter;
import com.example.simplemvc.shared.properties.SecurityProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final SecurityProperties securityProperties;
  private final JwtRequestFilter jwtRequestFilter;
  private final ObjectMapper objectMapper;
  @Value("${spring.mvc.servlet.path}")
  private String basePath;

  @Bean
  @Order(1)
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    log.info("Base path de la aplicación: {}", basePath);
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(FormLoginConfigurer::disable)
        .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.NEVER))
        .authorizeHttpRequests(auth -> {
          auth
              .requestMatchers(securityProperties.getPublicRoutes()).permitAll()
              .anyRequest().authenticated();

          provideRoutes(securityProperties.getNoAdminOperationToSelfRoutes(), (method, paths) -> auth
              .requestMatchers(method, paths).access(
                  (authentication, object) -> new AuthorizationDecision(isAdminAndNotSelf(authentication, object))));

          provideRoutes(securityProperties.getAdminRoutes(), (method, paths) -> auth
              .requestMatchers(method, paths).hasAuthority(UsuarioRol.ADMIN.name()));

          auth.anyRequest().authenticated();
        })
        .exceptionHandling((exceptionHandling) -> exceptionHandling
            .authenticationEntryPoint(this::manageNoAuthorized)
            .accessDeniedHandler(this::manageNoAuthorized))
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  @Order(2)
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOrigins(Arrays.asList(securityProperties.getAllowedOrigins()));
    config.setAllowedMethods(Arrays.asList(securityProperties.getAllowedMethods()));
    config.setAllowedHeaders(Arrays.asList(securityProperties.getAllowedHeaders()));
    config.setAllowCredentials(securityProperties.isAllowCredentials());

    for (String exposedHeader : securityProperties.getExposedHeaders()) {
      config.addExposedHeader(exposedHeader);
    }

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  private void manageNoAuthorized(HttpServletRequest request, HttpServletResponse response, RuntimeException e)
      throws JsonProcessingException, IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json;charset=UTF-8");
    String message = e.getMessage() != null ? e.getMessage() : "No autorizado";
    String json = objectMapper.writeValueAsString(new IllegalArgumentException(message));
    response.getWriter().write(json);
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

  private boolean isAdminAndNotSelf(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
    Authentication auth = authentication.get();

    if (auth == null) {
      return false;
    }

    if (!(auth.getPrincipal() instanceof Usuario)) {
      return false;
    }

    Usuario usuario = (Usuario) auth.getPrincipal();

    if (!usuario.getAuthorities().contains(UsuarioRol.ADMIN)) {
      return false;
    }

    String id = context.getVariables().get("id").toString();

    if (id == null) {
      log.warn("El id no ha sido proveído: {}", context.getRequest().getRequestURI());
      return false;
    }

    return !id.equals(usuario.getId().toString());
  }
}
