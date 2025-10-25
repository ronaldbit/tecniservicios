package com.example.simplemvc.config;

import com.example.simplemvc.security.CustomAuthFailureHandler;
import com.example.simplemvc.security.CustomAuthSuccessHandler;
import com.example.simplemvc.security.DbUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  // El constructor sigue aquí, Spring inyectará los beans aunque no los usemos temporalmente
  private final DbUserDetailsService uds;
  private final CustomAuthSuccessHandler success;
  private final CustomAuthFailureHandler failure;

  public SecurityConfig(DbUserDetailsService uds, CustomAuthSuccessHandler success, CustomAuthFailureHandler failure) {
   this.uds = uds; this.success = success; this.failure = failure;
  }

  // ===================================================================
  //   CONFIGURACIÓN TEMPORAL PARA TESTEO DE VISTAS
  // =CAL-COMENTA ESTE BLOQUE Y DESCOMENTA EL ORIGINAL PARA VOLVER
  // ===================================================================

  @Bean
  @Order(1) // Le damos orden 1 para que sea la única que se aplique
  SecurityFilterChain testeoDeVistasChain(HttpSecurity http) throws Exception {
   http
    .authorizeHttpRequests(auth -> auth
     .anyRequest().permitAll() // Permite TODAS las peticiones
    )
    .csrf(csrf -> csrf.disable()); // Mantenemos CSRF deshabilitado
   
   // No hay .formLogin(), ni .logout(), ni .rememberMe()
   // No es necesario el CaptchaFilter
   
   return http.build();
  }


  // ===================================================================
  //   CONFIGURACIÓN ORIGINAL (COMENTADA)
  // ===================================================================

  /*
  @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

  @Bean AuthenticationManager authenticationManager() {
   DaoAuthenticationProvider p = new DaoAuthenticationProvider();
   p.setUserDetailsService(uds);
   p.setPasswordEncoder(passwordEncoder());
   return new ProviderManager(p);
  }

  // Chain 1: SOLO para /admin/** y /api/admin/**
  @Bean
  @Order(1)
  SecurityFilterChain adminChain(HttpSecurity http) throws Exception {
   http
    .securityMatcher("/admin/**", "/api/admin/**")
    .authorizeHttpRequests(auth -> auth
     .requestMatchers("/assets/**", "/assets_shop/**", "/captcha").permitAll()
     .anyRequest().hasAnyRole("ADMIN","STAFF")
    )
    .formLogin(login -> login
     .loginPage("/admin/login")
     .loginProcessingUrl("/auth/login")  // ambos formularios POSTean aquí
     .successHandler(success)
     .failureHandler(failure)
     .permitAll()
    )
    .rememberMe(rm -> rm
     .rememberMeParameter("remember-me")
     .rememberMeCookieName("REMEMBERME")
     .tokenValiditySeconds(30*24*3600)
     .userDetailsService(uds)
     .key("cambia-esta-clave-secreta-robusta")
    )
    .logout(lo -> lo.logoutUrl("/logout").logoutSuccessUrl("/"))
    .csrf(csrf -> csrf.disable());

   // http.addFilterBefore(new CaptchaFilter(), UsernamePasswordAuthenticationFilter.class);
   return http.build();
  }

  // Chain 2: resto de rutas (tienda y públicas)
  @Bean
  @Order(2)
  SecurityFilterChain appChain(HttpSecurity http) throws Exception {
   http
    .authorizeHttpRequests(auth -> auth
     .requestMatchers("/", "/login", "/tienda/login", "/tienda/registro",
             "/auth/forgot", "/auth/reset",
             "/assets/**", "/assets_shop/**", "/captcha",
             "/api/tienda/**").permitAll()
     .anyRequest().authenticated()
    )
    .formLogin(login -> login
     .loginPage("/tienda/login")
     .loginProcessingUrl("/auth/login")
     .successHandler(success)
     .failureHandler(failure)
     .permitAll()
    )
    .rememberMe(rm -> rm
     .rememberMeParameter("remember-me")
     .rememberMeCookieName("REMEMBERME")
     .tokenValiditySeconds(30*24*3600)
     .userDetailsService(uds)
     .key("cambia-esta-clave-secreta-robusta")
    )
    .logout(lo -> lo.logoutUrl("/logout").logoutSuccessUrl("/"))
    .csrf(csrf -> csrf.disable());

   // http.addFilterBefore(new CaptchaFilter(), UsernamePasswordAuthenticationFilter.class);
   return http.build();
  }
  */
}
