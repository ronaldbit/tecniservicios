package com.example.simplemvc.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.simplemvc.dto.JwtDto;
import com.example.simplemvc.request.LoginUsuarioRequest;
import com.example.simplemvc.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @GetMapping("/login")
  public String loginView() {
    return "tienda/login";
  }

  @PostMapping("/login")
  public String login(LoginUsuarioRequest request, HttpServletResponse response, Model model) {
    try {
      JwtDto jwt = authService.login(request);

      if (jwt != null) {
        model.addAttribute("message", "Login exitoso. JWT: " + jwt.getJwt());

        Cookie jwtCookie = new Cookie("JWT_TOKEN", jwt.getJwt());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60);

        response.addCookie(jwtCookie);
      } else {
        model.addAttribute("message", "El login ha fallado");
      }

      return "tienda/home";
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
      return "tienda/login";
    }
  }

  @GetMapping("/admin/login")
  public String adminLoginView() {
    return "admin/login";
  }

  @GetMapping("/forgot")
  public String forgotView() {
    return "auth/forgot";
  }

  @GetMapping("/reset")
  public String resetView() {
    return "auth/reset";
  }

  @GetMapping("/registro")
  public String registroView() {
    return "tienda/registro";
  }
}
