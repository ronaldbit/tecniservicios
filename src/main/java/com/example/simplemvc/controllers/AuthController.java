package com.example.simplemvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

  // Raíz /login -> login de tienda (para que /login funcione)
  @GetMapping("/login")
  public String rootLogin() { return "tienda/login"; }

  @GetMapping("/tienda/login")
  public String tiendaLogin() { return "tienda/login"; }

  @GetMapping("/admin/login")
  public String adminLogin() { return "admin/login"; }

  // Vistas de recuperación / registro (implementar luego)
  @GetMapping("/auth/forgot")
  public String forgotView() { return "auth/forgot"; }

  @GetMapping("/auth/reset")
  public String resetView() { return "auth/reset"; }

  @GetMapping("/tienda/registro")
  public String registroView() { return "tienda/registro"; }
}
