package com.example.simplemvc.shared.config;

import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.service.JwtAuthenticationService;
import com.example.simplemvc.shared.properties.SecurityProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

  private final JwtAuthenticationService jwtAuthenticationService;
  private final SecurityProperties securityProperties;

  @Override
  public boolean preHandle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler)
      throws Exception {

    String requestURI = request.getRequestURI();
    String jwt = extractJwt(request);
    Usuario usuario = jwtAuthenticationService.fromJwt(jwt);

    if (requestURI.equals("/auth/verify") || requestURI.equals("/auth/reset-password")) {
      return true;
    }

    AntPathMatcher matcher = new AntPathMatcher();
    boolean isProtected = securityProperties.getProtectedRoutes().stream()
        .anyMatch(route -> matcher.match(route, requestURI));
    boolean isAuthEntry = securityProperties.getAuthEntryRoutes().stream()
        .anyMatch(route -> matcher.match(route, requestURI));

    if (isProtected && usuario == null) {
      response.sendRedirect("/auth/login");
      return false;
    }

    if (isAuthEntry && usuario != null) {
      usuario.getRoles().stream()
          .filter(role -> "CLIENTE".equalsIgnoreCase(role.getNombre()))
          .findFirst()
          .ifPresentOrElse(
              role -> {
                try {
                  response.sendRedirect("/home");
                } catch (Exception e) {
                  e.printStackTrace();
                }
              },
              () -> {
                try {
                  response.sendRedirect("/dashboard/portal");
                } catch (Exception e) {
                  e.printStackTrace();
                }
              });

      return false;
    }

    return true;
  }

  private String extractJwt(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return null;
    }

    for (Cookie cookie : cookies) {
      if ("JWT_TOKEN".equals(cookie.getName())) {
        return cookie.getValue();
      }
    }

    return null;
  }
}
