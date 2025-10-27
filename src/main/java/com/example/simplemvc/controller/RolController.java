package com.example.simplemvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.simplemvc.service.UsuarioRolService;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/dashboard/ajustes/roles")
@AllArgsConstructor
public class RolController {
  @Autowired
  private final UsuarioRolService usuarioRolService;

  @GetMapping
  public String lista(Model model) {
    model.addAttribute("roles", usuarioRolService.listarRoles());
    return "dashboard/ajustes/roles";
  }
}
