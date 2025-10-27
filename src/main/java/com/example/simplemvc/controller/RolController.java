package com.example.simplemvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.model.UsuarioMapper;
import com.example.simplemvc.service.GetCurrentUsuarioService;
import com.example.simplemvc.service.UsuarioRolService;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/dashboard/ajustes/roles")
@AllArgsConstructor
public class RolController {
  @Autowired
  private final UsuarioRolService usuarioRolService;

  @Autowired
  private final GetCurrentUsuarioService getCurrentUsuarioService;

  @Autowired
  private final UsuarioMapper usuarioMapper;

  @GetMapping
  public String lista(Model model) {
    Usuario currentUsuario = getCurrentUsuarioService.get();

    model.addAttribute("usuario", usuarioMapper.toDto(currentUsuario));
    model.addAttribute("roles", usuarioRolService.listarRoles());
    return "dashboard/ajustes/roles";
  }
}
