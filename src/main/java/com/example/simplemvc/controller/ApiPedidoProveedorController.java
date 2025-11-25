package com.example.simplemvc.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.simplemvc.dto.PedidoProveedorDto;
import com.example.simplemvc.request.CotizacionRequest;
import com.example.simplemvc.request.CrearPedidoProveedorRequest;
import com.example.simplemvc.request.DetalleReciboRequest;
import com.example.simplemvc.service.PedidoProveedorService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pedidos-proveedor")
@RequiredArgsConstructor
public class ApiPedidoProveedorController {

  private final PedidoProveedorService pedidoProveedorService;

  @Value("${spring.doc.path}")
  private String docUploadPath;

  @PostMapping
  public ResponseEntity<?> crear(@RequestBody CrearPedidoProveedorRequest request) {
    try {
      PedidoProveedorDto nuevoPedido = pedidoProveedorService.crearPedido(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error al crear el pedido: " + e.getMessage());
    }
  }

  @GetMapping
  public ResponseEntity<?> listarTodos() {
    try {
      List<PedidoProveedorDto> pedidos = pedidoProveedorService.obtenerTodosLosPedidos();
      return ResponseEntity.ok(pedidos);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error al obtener los pedidos: " + e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
    try {
      PedidoProveedorDto pedido = pedidoProveedorService.obtenerPedidoPorId(id);
      return ResponseEntity.ok(pedido);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error al obtener el pedido: " + e.getMessage());
    }
  }

  @PutMapping("/{id}/cotizar")
  public void cotizarPedido(
      @PathVariable Long id,
      @RequestBody CotizacionRequest cotizacionRequest) {
    pedidoProveedorService.EstablecerCotizacionPedido(id, cotizacionRequest);
    cotizacionRequest.getDetalles().forEach(det -> {
      System.out.println("Detalle ID: " + det.getIdDetalle() + ", Precio Unitario: " + det.getPrecioUnitario());
    });
  }

  @PutMapping("/{id}/cancelar-cotizacion")
  public ResponseEntity<?> cancelarCotizacion(@PathVariable Long id) {
    try {
      pedidoProveedorService.cancelarCotizacion(id);
      return ResponseEntity.ok().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error al cancelar la cotización: " + e.getMessage());
    }
  }

  @PutMapping("/{id}/aprobar")
  public ResponseEntity<?> confirmarAprobacion(@PathVariable Long id) {
    try {
      pedidoProveedorService.confirmarAprobacionporId(id);
      return ResponseEntity.ok().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error al confirmar el pedido: " + e.getMessage());
    }
  }

  @PutMapping("/{id}/cancelar")
  public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {
    try {
      pedidoProveedorService.cancelarPedidoPorId(id);
      return ResponseEntity.ok().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error al cancelar el pedido: " + e.getMessage());
    }
  }

  @PutMapping("/{id}/recibo-parcial")
  public ResponseEntity<?> actualizarReciboParcial(
      @PathVariable Long id,
      @RequestBody List<DetalleReciboRequest> detallesRequests) {
    try {
      pedidoProveedorService.reciboParcialPedidoPorId(id, detallesRequests);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(e.getMessage());
    }
  }

  @PostMapping("/confirmar/{id}")
  public ResponseEntity<?> confirmarPedidoConFactura(
      @PathVariable Long id,
      @RequestParam("factura") MultipartFile file) {
    try {
      String nombreGuardado = null;
      if (file != null && !file.isEmpty()) {
        String nombreOriginal = file.getOriginalFilename();
        String nombreLimpio = nombreOriginal.replaceAll("\\s+", "_");
        nombreGuardado = UUID.randomUUID() + "_" + nombreLimpio;
        Path path = Paths.get(docUploadPath).resolve(nombreGuardado);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());
        System.out.println("Documento guardado en: " + path.toAbsolutePath());
      }
      pedidoProveedorService.confirmarPedidoPorId(id, nombreGuardado);
      return ResponseEntity.ok().body("Pedido confirmado y factura guardada");
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error al confirmar el pedido: " + e.getMessage());
    }
  }
}