package com.example.simplemvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.simplemvc.service.UsuarioService;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/dashboard/ajustes/usuarios")
@AllArgsConstructor
public class UsuarioController {
  @Autowired
  private UsuarioService usuarioService;

  @GetMapping
  public String lista(Model model) {
    model.addAttribute("usuarios", usuarioService.listaTodos());
    return "dashboard/ajustes/usuarios";
  }
}
