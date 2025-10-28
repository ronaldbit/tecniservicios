package com.example.simplemvc.security;

import jakarta.servlet.http.*;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {
  @Override
  public void onAuthenticationFailure(
      HttpServletRequest req, HttpServletResponse resp, AuthenticationException ex)
      throws IOException {
    String scope = req.getParameter("scope");
    if ("admin".equalsIgnoreCase(scope)) resp.sendRedirect("/admin/login?error");
    else resp.sendRedirect("/tienda/login?error");
  }
}
