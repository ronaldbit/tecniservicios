package com.example.simplemvc.controller;

import com.example.simplemvc.dto.VentaDto;
import com.example.simplemvc.request.CrearVentaRequest;
import com.example.simplemvc.service.VentaService;
import com.example.simplemvc.shared.Exeption.StockInsuficienteException;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; // Usando @Controller como en tu ejemplo
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/siguiente-numero")
    public ResponseEntity<?> obtenerSiguienteNumero(@RequestParam String tipo) {
        try {
            if (!tipo.equalsIgnoreCase("Boleta") && !tipo.equalsIgnoreCase("Factura")) {
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