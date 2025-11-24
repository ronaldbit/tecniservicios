package com.example.simplemvc.config;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.simplemvc.model.Permiso;
import com.example.simplemvc.model.Rol;
import com.example.simplemvc.repository.RolRepository; // Asegúrate de tener este repo
import com.example.simplemvc.shared.filter.JwtRequestFilter;
import com.example.simplemvc.shared.properties.SecurityProperties;

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
  private final RolRepository rolRepository;

  @Bean
  @Order(1)
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> {
          auth.requestMatchers(
              "/",
              "/home/**",
              "/auth/**",
              "/api/**",
              "/assets/**",
              "/assets_shop/**",
              "/css/**",
              "/js/**",
              "/images/**",
              "/uploads/**",
              "/fonts/**",
              "/errors/**").permitAll();
          try {
            List<Rol> roles = rolRepository.findAll();
            Map<String, List<String>> rolesPorRuta = roles.stream()
                .flatMap(rol -> rol.getPermisos().stream()
                    .map(permiso -> new RolPermisoPair(rol, permiso)))
                .collect(Collectors.groupingBy(
                    pair -> pair.permiso.getPath(),
                    Collectors.mapping(pair -> "ROLE_" + pair.rol.getNombre().toUpperCase(),
                        Collectors.toList())));
            rolesPorRuta.entrySet().stream()
                .sorted(Comparator
                    .comparingInt(
                        (Map.Entry<String, List<String>> entry) -> entry.getKey().length())
                    .reversed())
                .forEach(entry -> {
                  String path = entry.getKey();
                  String[] authorities = entry.getValue().toArray(new String[0]);
                  log.info("Regla creada: Ruta '{}' permite {}", path,
                      java.util.Arrays.toString(authorities));
                  auth.requestMatchers(path).hasAnyAuthority(authorities);
                });
          } catch (Exception e) {
            log.error("Error al cargar permisos desde BD: {}", e.getMessage());
          }

          auth.anyRequest().authenticated();
        })
        .headers(headers -> headers
            .contentSecurityPolicy(csp -> csp
                .policyDirectives(
                    "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://cdn.tailwindcss.com https://unpkg.com https://cdn.jsdelivr.net; "
                        +
                        "style-src 'self' 'unsafe-inline' https://cdn.tailwindcss.com https://unpkg.com https://cdn.jsdelivr.net; "
                        +
                        "img-src 'self' data: https:; " +
                        "font-src 'self' data: https:;")))
        .exceptionHandling(ex -> ex
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              response.sendRedirect("/errors/403");
            })
            .authenticationEntryPoint((request, response, authException) -> {
              response.sendRedirect("/auth/login");
            }))
        .logout(logout -> logout
            .logoutUrl("/auth/logout")
            .logoutSuccessHandler((request, response, authentication) -> {
              if (request.getSession(false) != null) {
                request.getSession().invalidate();
              }
              response.addHeader("Set-Cookie", "JWT_TOKEN=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax");
              response.addHeader("Set-Cookie", "JSESSIONID=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax");
              response.sendRedirect("/auth/login");
            }))
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  private record RolPermisoPair(Rol rol, Permiso permiso) {
  }

  @Bean
  @Order(2)
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(
        securityProperties.getAllowedOrigins() != null ? securityProperties.getAllowedOrigins() : List.of("*"));
    config.setAllowedMethods(securityProperties.getAllowedMethods() != null ? securityProperties.getAllowedMethods()
        : List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(false);

    if (securityProperties.getExposedHeaders() != null) {
      config.setExposedHeaders(securityProperties.getExposedHeaders());
    }

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}