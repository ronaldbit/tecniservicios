package com.example.simplemvc.controller;

import java.util.List;

import com.example.simplemvc.request.ActualizarRequest;
import com.example.simplemvc.request.CrearProductoRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.simplemvc.dto.ProductoDto;
import com.example.simplemvc.service.ProductoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ApiProductoController {
    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDto>> listarProductos() {
        List<ProductoDto> productos = productoService.findAll();
        return ResponseEntity.ok(productos);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearProducto(@RequestBody CrearProductoRequest request) {
        try {
            ProductoDto nuevoProducto = productoService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody CrearProductoRequest request) {
        try {
            ProductoDto productoActualizado = productoService.update(id, request);
            return ResponseEntity.ok(productoActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.delete(id);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDto>> buscarProductos(@RequestParam String query) {
        List<ProductoDto> resultados = productoService.buscarProductos(query);
        return ResponseEntity.ok(resultados);
    }
    @PostMapping("/actualizarInventario")
    public ResponseEntity<?> actualizarInventario(@RequestBody ActualizarRequest request) {
        try {
            productoService.actualizarInventario(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
