package com.example.simplemvc.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.repository.CajaRepository;
import com.example.simplemvc.service.GetActualUsuarioService;
import com.example.simplemvc.service.ReportesService;
import com.example.simplemvc.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/dashboard/reportes")
@RequiredArgsConstructor
public class ReportesViewController {

  private final CajaRepository cajaRepository;
  private final GetActualUsuarioService getActualUsuarioService;
  private final ReportesService reportesService;
  private final UsuarioService usuarioService;

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
  @Transactional(readOnly = true)
  public String verReportesVentas(Model model,
      @RequestParam(required = false) String inicio,
      @RequestParam(required = false) String fin,
      @RequestParam(required = false) Long usuarioId) { // Nuevo Param

    Usuario actualUsuario = getActualUsuarioService.get();
    model.addAttribute("usuario", actualUsuario);

    actualUsuario.getRoles().stream()
        .flatMap(rol -> rol.getPermisos().stream())
        .forEach(permiso -> model.addAttribute("hide" + permiso.getNombre(), false));

    model.addAttribute("listaUsuarios", usuarioService.listaTodos().stream()
        .filter(u -> u.getRoles().stream()
            .anyMatch(r -> "VENDEDOR".equals(r.getNombre()) || "CAJERO".equals(r.getNombre())))
        .toList());

    model.addAttribute("pageTitle", "Reportes de Ventas");

    if (inicio != null && fin != null && !inicio.isEmpty() && !fin.isEmpty()) {
      LocalDate fInicio = LocalDate.parse(inicio);
      LocalDate fFin = LocalDate.parse(fin);
      Map<String, Object> reporte = reportesService.obtenerReporteVentas(fInicio, fFin, usuarioId);

      model.addAllAttributes(reporte);

      model.addAttribute("fechaInicioSel", fInicio);
      model.addAttribute("fechaFinSel", fFin);
      model.addAttribute("usuarioIdSel", usuarioId);
    } else {
      model.addAttribute("fechaInicioSel", LocalDate.now().withDayOfMonth(1));
      model.addAttribute("fechaFinSel", LocalDate.now());
    }

    return "dashboard/reportes/index_ventas";
  }

  // ANALISIS DE TOP 10 PRODUCTOS
  @GetMapping("/analisis")
  @Transactional(readOnly = true)
  public String verReportesAnalisis(Model model,
      @RequestParam(required = false) String inicio,
      @RequestParam(required = false) String fin,
      @RequestParam(required = false) String tipo) {
    Usuario actualUsuario = getActualUsuarioService.get();
    model.addAttribute("usuario", actualUsuario);

    actualUsuario.getRoles().stream()
        .flatMap(rol -> rol.getPermisos().stream())
        .forEach(permiso -> model.addAttribute("hide" + permiso.getNombre(), false));

    model.addAttribute("pageTitle", "Análisis de Productos");

    if (inicio != null && fin != null && tipo != null) {
      LocalDate fInicio = LocalDate.parse(inicio);
      LocalDate fFin = LocalDate.parse(fin);

      if ("productos".equals(tipo)) {
        Map<String, Object> data = reportesService.obtenerReporteTopProductos(fInicio, fFin);
        model.addAttribute("reporteProductos", data);
        model.addAttribute("tabActivo", "productos");
      } else if ("vendedores".equals(tipo)) {
        Map<String, Object> data = reportesService.obtenerRankingVendedores(fInicio, fFin);
        model.addAttribute("reporteVendedores", data);
      }

      model.addAttribute("fechaInicioSel", fInicio);
      model.addAttribute("fechaFinSel", fFin);
    } else {

      model.addAttribute("fechaInicioSel", LocalDate.now().withDayOfMonth(1));
      model.addAttribute("fechaFinSel", LocalDate.now());
    }

    return "dashboard/reportes/index_analisis";
  }

}