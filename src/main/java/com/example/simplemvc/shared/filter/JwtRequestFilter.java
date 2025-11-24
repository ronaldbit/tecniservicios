package com.example.simplemvc.shared.filter;

import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.service.JwtAuthenticationService;
import com.example.simplemvc.shared.ClienteSesion;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
  private final JwtAuthenticationService jwtAuthenticationService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String method = request.getMethod();
    String uri = request.getRequestURI();

    if (uri.startsWith("/assets/") ||
        uri.startsWith("/css/") ||
        uri.startsWith("/js/") ||
        uri.startsWith("/images/") ||
        uri.startsWith("/fonts/")) {
      filterChain.doFilter(request, response);
      return;
    }

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
      log.error(
          "No se pudo extraer el usuario del token JWT en la solicitud a {}.",
          request.getRequestURI());

      filterChain.doFilter(request, response);
      return;
    }

    HttpSession session = request.getSession(true);
    if (session.getAttribute("CLIENTE_SESION") == null) {

      var persona = usuario.getPersona();
      if (persona != null) {
        ClienteSesion clienteSesion = new ClienteSesion();
        clienteSesion.setIdCliente(persona.getId());

        String nombreCompleto = (persona.getNombres() != null ? persona.getNombres() : "") + " " +
            (persona.getApellidos() != null ? persona.getApellidos() : "");
        clienteSesion.setNombreCompleto(nombreCompleto.trim());

        clienteSesion.setEmail(persona.getEmail());
        clienteSesion.setDni(persona.getNumeroDocumento());
        session.setAttribute("clienteSesion", clienteSesion);
        boolean esCliente = usuario.getRoles().stream()
            .anyMatch(rol -> "CLIENTE".equalsIgnoreCase(rol.getNombre()));
        boolean esPersonal = !esCliente;    
        session.setAttribute("ES_PERSONAL", esPersonal);

        log.debug("Sesión re-hidratada para usuario: {}", usuario.getUsername());
      }
    }

    log.debug("Jwt válido recibido en la solicitud a {}.", request.getRequestURI());
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuario, null,
        usuario.getAuthorities());

    System.out.println(usuario.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    log.info("Usuario {} autenticado satisfactoriamente", usuario.getNombreUsuario());

    filterChain.doFilter(request, response);
  }
}
