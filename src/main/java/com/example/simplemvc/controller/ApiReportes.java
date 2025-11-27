package com.example.simplemvc.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simplemvc.dto.VentaDto;
import com.example.simplemvc.service.PdfService;
import com.example.simplemvc.service.VentaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ApiReportes {

  private final VentaService ventaService;
  private final PdfService pdfService;

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
}