package com.example.simplemvc.shared.filter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.service.JwtAuthenticationService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
  private final JwtAuthenticationService jwtAuthenticationService;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String method = request.getMethod();
    String uri = request.getRequestURI();

    log.info("Incoming request: {} {}", method, uri);

    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (!StringUtils.hasText(authorizationHeader) && request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("JWT_TOKEN".equals(cookie.getName())) {
          authorizationHeader = "Bearer " + cookie.getValue();
          break;
        }
      }
    }

    if (!(StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer "))) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authorizationHeader.substring(7);

    if (!jwtAuthenticationService.isValid(token)) {
      log.warn("Token JWT inválido recibido en la solicitud a {}.", request.getRequestURI());

      filterChain.doFilter(request, response);
      return;
    }

    Usuario usuario = jwtAuthenticationService.fromJwt(token);

    if (usuario == null) {
      log.error("No se pudo extraer el usuario del token JWT en la solicitud a {}.", request.getRequestURI());

      filterChain.doFilter(request, response);
      return;
    }

    log.debug("Jwt válido recibido en la solicitud a {}.", request.getRequestURI());
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuario, null,
        usuario.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    log.info("Usuario {} autenticado satisfactoriamente", usuario.getCorreo());

    filterChain.doFilter(request, response);
  }
}
