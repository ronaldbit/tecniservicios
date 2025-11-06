package com.example.simplemvc.controller;

import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.model.UsuarioMapper;
import com.example.simplemvc.service.GetActualUsuarioService;
import com.example.simplemvc.service.RolService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard/ajustes/roles")
@AllArgsConstructor
public class RolController {
  @Autowired private final RolService rolService;

  @Autowired private final GetActualUsuarioService getActualUsuarioService;

  @Autowired private final UsuarioMapper usuarioMapper;
  
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public String lista(Model model) {
    Usuario usuario = getActualUsuarioService.get();

    if (usuario == null) {
      return "redirect:/auth/login";
    }

    model.addAttribute("usuario", usuarioMapper.toDto(usuario));
    model.addAttribute("roles", rolService.lista());

    usuario.getRoles().stream()
        .flatMap(rol -> rol.getPermisos().stream())
        .forEach(permiso -> model.addAttribute("hide" + permiso.getNombre(), false));

    return "/dashboard/ajustes/roles";
  }
}
