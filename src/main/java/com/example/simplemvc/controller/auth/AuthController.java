package com.example.simplemvc.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

  @GetMapping("/login")
  public String loginView() {
    return "tienda/login";
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
