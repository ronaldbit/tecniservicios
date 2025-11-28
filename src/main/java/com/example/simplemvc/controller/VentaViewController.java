package com.example.simplemvc.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.simplemvc.model.CajaSesion;
import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.service.CajaService;
import com.example.simplemvc.service.GetActualUsuarioService;

@Controller
@RequestMapping("/dashboard/ventas/venta-normal")

public class VentaViewController {

  @Autowired
  private GetActualUsuarioService getActualUsuarioService;

  @Autowired
  private CajaService cajaService;

  @GetMapping
  public String verCaja(Model model) {

    Usuario usuario = getActualUsuarioService.get();
    model.addAttribute("usuario", usuario);

    usuario.getRoles().stream()
        .flatMap(rol -> rol.getPermisos().stream())
        .forEach(permiso -> model.addAttribute("hide" + permiso.getNombre(), false));

    Optional<CajaSesion> sesion = cajaService.obtenerSesionActualOpcional(1L);

    if (sesion.isEmpty()) {
      model.addAttribute("estadoCaja", "cerrada");
      return "dashboard/ventas/venta-normal";
    }

    model.addAttribute("estadoCaja", "ok");

    return "dashboard/ventas/venta-normal";
  }

}