package com.example.simplemvc.controller;

import com.example.simplemvc.dto.PedidoProveedorDto;
import com.example.simplemvc.request.CrearPedidoProveedorRequest;
import com.example.simplemvc.service.PedidoProveedorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos-proveedor")
@AllArgsConstructor
public class ApiPedidoProveedorController {

    private final PedidoProveedorService pedidoProveedorService;

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
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarPedido(@PathVariable Long id) {
        try {
            pedidoProveedorService.confirmarPedidoPorId(id);
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
}