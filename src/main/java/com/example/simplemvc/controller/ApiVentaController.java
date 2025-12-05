package com.example.simplemvc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; // Usando @Controller como en tu ejemplo
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.simplemvc.dto.VentaDto;
import com.example.simplemvc.request.CrearVentaRequest;
import com.example.simplemvc.service.VentaService;
import com.example.simplemvc.shared.Exeption.StockInsuficienteException;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/ventas")
@AllArgsConstructor
public class ApiVentaController {

  private final VentaService ventaService;

  @PostMapping
  public ResponseEntity<?> crearVenta(@RequestBody CrearVentaRequest request) {
    try {
      VentaDto nuevaVenta = ventaService.crearVenta(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);

    } catch (StockInsuficienteException | EntityNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());

    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error interno al registrar la venta: " + e.getMessage());
    }
  }

  @GetMapping
  public ResponseEntity<?> listarVentas() {
    try {
      List<VentaDto> ventas = ventaService.listarTodasLasVentas();
      return ResponseEntity.ok(ventas);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error al listar las ventas: " + e.getMessage());
    }
  }

  @GetMapping("/online")
  @Transactional(readOnly = true)
  public ResponseEntity<?> listarVentasOnline() {
    try {
      List<VentaDto> ventas = ventaService.listarVentasOnline();
      return ResponseEntity.ok(ventas);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error al listar las ventas: " + e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> obtenerVenta(@PathVariable Long id) {
    try {
      VentaDto venta = ventaService.obtenerVentaPorId(id);
      return ResponseEntity.ok(venta);

    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error al obtener la venta: " + e.getMessage());
    }
  }

  @PutMapping("/{id}/anular")
  public ResponseEntity<?> anularVenta(@PathVariable Long id) {
    try {
      VentaDto ventaAnulada = ventaService.anularVenta(id);
      return ResponseEntity.ok(ventaAnulada);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error al anular la venta: " + e.getMessage());
    }
  }

  @PutMapping("/complete-online/{id}")
  public ResponseEntity<?> completarVentaOnline(@PathVariable Long id) {
    ventaService.completarVentaOnline(id);
    return ResponseEntity.ok(Map.of("message", "Venta completada"));
  }

  @GetMapping("/siguiente-numero")
  public ResponseEntity<?> obtenerSiguienteNumero(@RequestParam String tipo) {
    try {
      if (!"Boleta".equalsIgnoreCase(tipo) && !"Factura".equalsIgnoreCase(tipo)) {
        return ResponseEntity.badRequest().body("Tipo de comprobante no válido");
      }
      String nuevoNumero = ventaService.generarSiguienteNumero(tipo);
      return ResponseEntity.ok(Map.of("numero", nuevoNumero));

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error al generar el número: " + e.getMessage());
    }
  }
}