package com.example.simplemvc.controller.usuario;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
  @GetMapping
  public String mainView() {
    return "usuarios";
  }
}
