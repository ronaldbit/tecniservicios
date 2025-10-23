package com.example.simplemvc.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class CaptchaFilter extends OncePerRequestFilter {
  private static final String SESSION_KEY = "CAPTCHA_CODE";

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
      throws ServletException, IOException {

    if ("POST".equalsIgnoreCase(req.getMethod()) && "/auth/login".equals(req.getServletPath())) {
      HttpSession session = req.getSession(false);
      String expected = session != null ? (String) session.getAttribute(SESSION_KEY) : null;
      String provided = req.getParameter("codigo");
      if (provided != null) provided = provided.trim().toUpperCase();
      if (session != null) session.removeAttribute(SESSION_KEY); // 1 uso

      if (expected == null || provided == null || !expected.equals(provided)) {
        String scope = req.getParameter("scope");
        if ("admin".equalsIgnoreCase(scope)) resp.sendRedirect("/admin/login?error=captcha");
        else resp.sendRedirect("/tienda/login?error=captcha");
        return;
      }
    }
    chain.doFilter(req, resp);
  }
}
