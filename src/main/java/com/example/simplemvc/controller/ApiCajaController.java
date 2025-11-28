package com.example.simplemvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simplemvc.request.AbrirCajaRequest;
import com.example.simplemvc.request.CerrarCajaRequest;
import com.example.simplemvc.request.MovimientoCajaRequest;
import com.example.simplemvc.service.CajaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/caja")
@RequiredArgsConstructor
public class ApiCajaController {

  private final CajaService cajaService;

  @PostMapping("/abrir")
  public ResponseEntity<?> abrir(@RequestBody AbrirCajaRequest request) {
    try {
      cajaService.abrirCaja(request);
      return ResponseEntity.ok("Caja abierta correctamente");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/movimiento")
  public ResponseEntity<?> registrarMovimiento(@RequestBody MovimientoCajaRequest request) {
    try {
      cajaService.registrarMovimientoManual(request);
      return ResponseEntity.ok("Movimiento registrado");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/cerrar")
  public ResponseEntity<?> cerrar(@RequestBody CerrarCajaRequest request) {
    try {
      cajaService.cerrarCaja(request);
      return ResponseEntity.ok("Caja cerrada correctamente");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
