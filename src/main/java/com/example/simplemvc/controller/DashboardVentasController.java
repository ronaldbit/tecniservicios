package com.example.simplemvc.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.simplemvc.dto.DashboardVentasDto;
import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.service.DashboardService;
import com.example.simplemvc.service.GetActualUsuarioService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardVentasController {

  private final DashboardService dashboardService;
  private final GetActualUsuarioService getActualUsuarioService;

  @GetMapping
  public String dashboard(Model model) {
    Usuario usuario = getActualUsuarioService.get();
    model.addAttribute("usuario", usuario);
    usuario.getRoles().stream()
        .flatMap(rol -> rol.getPermisos().stream())
        .forEach(permiso -> model.addAttribute("hide" + permiso.getNombre(), false));
    DashboardVentasDto metricas = dashboardService.obtenerMetricas();
    model.addAttribute("kpi", metricas);
    List<BigDecimal> graficoAnual = dashboardService.obtenerGraficoAnual();
    model.addAttribute("graficoAnual", graficoAnual);
    model.addAttribute("pieFisica", metricas.getIngresosTiendaFisica());
    model.addAttribute("pieOnline", metricas.getIngresosTiendaOnline());
    return "dashboard/index";
  }
}