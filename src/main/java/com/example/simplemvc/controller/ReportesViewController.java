package com.example.simplemvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.repository.CajaRepository;
import com.example.simplemvc.service.GetActualUsuarioService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/dashboard/reportes")
@RequiredArgsConstructor
public class ReportesViewController {

  private final CajaRepository cajaRepository;
  private final GetActualUsuarioService getActualUsuarioService;

  @GetMapping("/caja")
  public String verReportesCaja(Model model) {
    cargarUsuarioSiderbar(model);
    model.addAttribute("pageTitle", "Reportes de Caja");
    model.addAttribute("cajas", cajaRepository.findAll());
    return "dashboard/reportes/index_caja";
  }

  public void cargarUsuarioSiderbar(Model model) {
    Usuario actualUsuario = getActualUsuarioService.get();
    model.addAttribute("actualUsuario", actualUsuario);
    actualUsuario.getRoles().stream()
        .flatMap(rol -> rol.getPermisos().stream())
        .forEach(permiso -> model.addAttribute("hide" + permiso.getNombre(), false));
  }
}