package com.example.simplemvc.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.model.UsuarioMapper;
import com.example.simplemvc.service.GetActualUsuarioService;
import com.example.simplemvc.service.RolService;
import com.example.simplemvc.service.UsuarioService;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/dashboard/ajustes/usuarios")
@AllArgsConstructor
public class UsuarioController {
  @Autowired
  private final UsuarioService usuarioService;
  @Autowired
  private final RolService rolService;
  @Autowired
  private final GetActualUsuarioService getActualUsuarioService;

  @Autowired
  private final UsuarioMapper usuarioMapper;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public String lista(Model model) {
    Usuario usuario = getActualUsuarioService.get();
    model.addAttribute("usuario", usuarioMapper.toDto(usuario));
    model.addAttribute("usuarios", usuarioService.listaTodos().stream()
        .filter(p -> !"CLIENTE".equals(p.getRoles().get(0).getNombre()))
        .collect(Collectors.toList()));
    model.addAttribute("roles", rolService.lista());
    usuario.getRoles().stream()
        .flatMap(rol -> rol.getPermisos().stream())
        .forEach(permiso -> model.addAttribute("hide" + permiso.getNombre(), false));
    return "dashboard/ajustes/usuarios";
  }
}
