package com.example.simplemvc.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.repository.CajaRepository;
import com.example.simplemvc.service.GetActualUsuarioService;
import com.example.simplemvc.service.ReportesService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/dashboard/reportes")
@RequiredArgsConstructor
public class ReportesViewController {

  private final CajaRepository cajaRepository;
  private final GetActualUsuarioService getActualUsuarioService;
  private final ReportesService reportesService;

  // REPORTE DE CAJA
  @GetMapping("/caja")
  public String verReportesCaja(Model model) {

    Usuario actualUsuario = getActualUsuarioService.get();
    model.addAttribute("usuario", actualUsuario);
    actualUsuario.getRoles().stream()
        .flatMap(rol -> rol.getPermisos().stream())
        .forEach(permiso -> model.addAttribute("hide" + permiso.getNombre(), false));
    model.addAttribute("pageTitle", "Reportes de Caja");
    model.addAttribute("cajas", cajaRepository.findAll());
    return "dashboard/reportes/index_caja";
  }

  // REPORTE DE INVENTARIO
  @GetMapping("/inventario")
  public String verReportesInventario(Model model) {
    Usuario actualUsuario = getActualUsuarioService.get();
    model.addAttribute("usuario", actualUsuario);

    actualUsuario.getRoles().stream()
        .flatMap(rol -> rol.getPermisos().stream())
        .forEach(permiso -> model.addAttribute("hide" + permiso.getNombre(), false));

    model.addAttribute("pageTitle", "Reportes de Inventario");

    model.addAttribute("stockBajo", reportesService.obtenerReporteStockBajo());
    model.addAttribute("valorizacion", reportesService.obtenerValorizacionInventario());

    return "dashboard/reportes/index_inventario";
  }

  // REPORTE DE VENTAS POR FECHA
  @GetMapping("/ventas")
  public String verReportesVentas(Model model) {
    Usuario actualUsuario = getActualUsuarioService.get();
    model.addAttribute("usuario", actualUsuario);
    actualUsuario.getRoles().stream()
        .flatMap(rol -> rol.getPermisos().stream())
        .forEach(permiso -> model.addAttribute("hide" + permiso.getNombre(), false));

    model.addAttribute("pageTitle", "Reportes de Ventas");
    model.addAttribute("fechaInicioDefault", LocalDate.now().withDayOfMonth(1));
    model.addAttribute("fechaFinDefault", LocalDate.now());

    return "dashboard/reportes/index_ventas";
  }

  // ANALISIS DE TOP 10 PRODUCTOS
  @GetMapping("/analisis")
  public String verReportesAnalisis(Model model) {
    Usuario actualUsuario = getActualUsuarioService.get();
    model.addAttribute("usuario", actualUsuario);
    actualUsuario.getRoles().stream()
        .flatMap(rol -> rol.getPermisos().stream())
        .forEach(permiso -> model.addAttribute("hide" + permiso.getNombre(), false));
    model.addAttribute("pageTitle", "Análisis de Productos");
    model.addAttribute("fechaInicioDefault", LocalDate.now().withDayOfYear(1));
    model.addAttribute("fechaFinDefault", LocalDate.now());

    return "dashboard/reportes/index_analisis";
  }

}