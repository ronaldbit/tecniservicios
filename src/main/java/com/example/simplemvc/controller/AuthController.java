package com.example.simplemvc.controller;

import com.example.simplemvc.service.SucursalService;
import com.example.simplemvc.service.TipoDocumentoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

  @Autowired private final TipoDocumentoService tipoDocumentoService;

  @Autowired private final SucursalService sucursalService;

  @GetMapping("/registro-persona")
  public String registroPersona(Model model) {
    model.addAttribute("tiposDocumento", tipoDocumentoService.lista());

    return "/auth/registro-persona";
  }

  @GetMapping("/registro")
  public String registroUsuario(Model model) {
    System.err.println("sucursalService.lista(): " + sucursalService.lista().size());
    model.addAttribute("sucursales", sucursalService.lista());

    if (model.getAttribute("persona") == null) {
      return "redirect:/auth/registro-persona";
    }

    return "/auth/registro";
  }
}
