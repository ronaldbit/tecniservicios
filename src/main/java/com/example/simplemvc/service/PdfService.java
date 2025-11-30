package com.example.simplemvc.service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.simplemvc.dto.VentaDto;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PdfService {

  private final TemplateEngine templateEngine;

  // VAUCHER DE VENTAS (GENERAR PDF)
  public byte[] generarPdfVenta(VentaDto venta) {
    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      Context context = new Context();
      context.setVariable("venta", venta);
      String htmlContent = templateEngine.process("dashboard/reportes/comprobante_venta", context);
      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.useFastMode();
      builder.withHtmlContent(htmlContent, "");
      builder.toStream(os);
      builder.run();
      return os.toByteArray();

    } catch (Exception e) {
      throw new RuntimeException("Error al generar el PDF", e);
    }
  }

  // REPORTE DE CAJA (GENERAR PDF)
  public byte[] generarPdfCaja(Map<String, Object> datos) {
    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      Context context = new Context();
      context.setVariables(datos);

      String htmlContent = templateEngine.process("dashboard/reportes/pdf_caja", context);

      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.useFastMode();
      builder.withHtmlContent(htmlContent, null);
      builder.toStream(os);
      builder.run();

      return os.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("Error generando PDF de caja", e);
    }
  }
}