package com.example.simplemvc.security;

import jakarta.servlet.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
  private boolean has(Authentication a, String role){
    for (GrantedAuthority ga: a.getAuthorities()) if (role.equalsIgnoreCase(ga.getAuthority())) return true;
    return false;
  }
  @Override
  public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth) throws IOException {
    String scope = req.getParameter("scope"); // "admin" o "tienda"
    boolean admin = has(auth, "ROLE_ADMIN") || has(auth, "ROLE_STAFF");
    boolean client = has(auth, "ROLE_CLIENT");

    if ("admin".equalsIgnoreCase(scope)) {
      if (!admin) { resp.sendRedirect("/admin/login?error=forbidden"); return; }
      resp.sendRedirect("/admin"); return;
    }
    // Tienda (por defecto)
    if (!(client || admin)) { resp.sendRedirect("/tienda/login?error=forbidden"); return; }
    resp.sendRedirect("/perfil"); // o "/"
  }
}
