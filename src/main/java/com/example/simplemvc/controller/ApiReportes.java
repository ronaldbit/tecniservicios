package com.example.simplemvc.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.simplemvc.dto.VentaDto;
import com.example.simplemvc.model.CajaSesion;
import com.example.simplemvc.model.Producto;
import com.example.simplemvc.service.PdfService;
import com.example.simplemvc.service.ReportesService;
import com.example.simplemvc.service.VentaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ApiReportes {

  private final ReportesService reportesService;
  private final VentaService ventaService;
  private final PdfService pdfService;

  // REPORTE DE VALORES DE VENTA EN PDF
  @GetMapping("/ventas/pdf/{id}")
  public ResponseEntity<byte[]> descargarPdfVenta(@PathVariable Long id) {
    VentaDto venta = ventaService.obtenerVentaPorId(id);
    byte[] pdfBytes = pdfService.generarPdfVenta(venta);
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=venta_" + venta.getNumeroComprobante() + ".pdf");
    return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdfBytes);
  }

  // METODOS DE REPORTE DE CAJA DIARIA EN PDF
  @GetMapping("/caja/sesiones")
  public ResponseEntity<List<CajaSesion>> buscarSesiones(
      @RequestParam Long cajaId,
      @RequestParam String fecha) {
    LocalDate localDate = LocalDate.parse(fecha);
    return ResponseEntity.ok(reportesService.buscarSesionesPorFecha(cajaId, localDate));
  }

  @GetMapping("/caja/pdf/{sesionId}")
  public ResponseEntity<byte[]> descargarPdfCaja(@PathVariable Long sesionId) {
    Map<String, Object> datos = reportesService.obtenerDatosReporteCaja(sesionId);

    byte[] pdfBytes = pdfService.generarPdfCaja(datos);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=caja_sesion_" + sesionId + ".pdf");

    return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdfBytes);
  }

  // REPORTE DE STOCK BAJO EN PDF
  @GetMapping("/inventario/stock-bajo/pdf")
  public ResponseEntity<byte[]> descargarPdfStockBajo() {
    List<Producto> productos = reportesService.obtenerReporteStockBajo();
    Map<String, Object> datos = new HashMap<>();
    datos.put("productos", productos);
    datos.put("fechaGeneracion", java.time.LocalDateTime.now());
    byte[] pdfBytes = pdfService.generarPdfGenerico("pdf_stock_bajo", datos);
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=reporte_stock_bajo.pdf");
    return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdfBytes);
  }

  @GetMapping("/ventas/reporte/pdf")
  @Transactional(readOnly = true)
  public ResponseEntity<byte[]> descargarPdfReporteVentas(
      @RequestParam String inicio,
      @RequestParam String fin,
      @RequestParam(required = false) Long usuarioId) {

    LocalDate fechaInicio = LocalDate.parse(inicio);
    LocalDate fechaFin = LocalDate.parse(fin);

    Map<String, Object> datos = reportesService.obtenerReporteVentas(fechaInicio, fechaFin, usuarioId);
    byte[] pdfBytes = pdfService.generarPdfGenerico("pdf_reporte_ventas", datos);
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=reporte_ventas.pdf");

    return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdfBytes);
  }

  // REPORTE DE TOP PRODUCTOS EN PDF
  @GetMapping("/analisis/top-productos/pdf")
  public ResponseEntity<byte[]> descargarPdfTopProductos(
      @RequestParam String inicio,
      @RequestParam String fin) {

    LocalDate fechaInicio = LocalDate.parse(inicio);
    LocalDate fechaFin = LocalDate.parse(fin);

    Map<String, Object> datos = reportesService.obtenerReporteTopProductos(fechaInicio, fechaFin);

    byte[] pdfBytes = pdfService.generarPdfGenerico("pdf_top_productos", datos);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=ranking_productos.pdf");

    return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdfBytes);
  }

  @GetMapping("/analisis/ranking-vendedores/pdf")
  public ResponseEntity<byte[]> descargarPdfRankingVendedores(
      @RequestParam String inicio,
      @RequestParam String fin) {

    LocalDate fechaInicio = LocalDate.parse(inicio);
    LocalDate fechaFin = LocalDate.parse(fin);

    Map<String, Object> datos = reportesService.obtenerRankingVendedores(fechaInicio, fechaFin);

    byte[] pdfBytes = pdfService.generarPdfGenerico("pdf_ranking_vendedores", datos);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=ranking_vendedores.pdf");

    return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdfBytes);
  }

}