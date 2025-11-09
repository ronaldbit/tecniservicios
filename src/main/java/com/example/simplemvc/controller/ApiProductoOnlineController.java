package com.example.simplemvc.controller;

import com.example.simplemvc.dto.ProductoOnlineDto;
import com.example.simplemvc.request.CrearProductoOnlineRequest;
import com.example.simplemvc.service.ProductoOnlineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/productos-online")
@RequiredArgsConstructor
public class ApiProductoOnlineController {

    private final ProductoOnlineService productoOnlineService;

    @GetMapping
    public ResponseEntity<List<ProductoOnlineDto>> listar() {
        return ResponseEntity.ok(productoOnlineService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoOnlineDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoOnlineService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearProductoOnlineRequest request) {
        try {
            ProductoOnlineDto nuevoProductoOnline = productoOnlineService.crear(request);
            return ResponseEntity.ok(nuevoProductoOnline);
        } catch (Exception e) {
            log.error("Error al crear ProductoOnline: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoOnlineDto> actualizar(@PathVariable Long id,
            @RequestBody CrearProductoOnlineRequest request) {
        return ResponseEntity.ok(productoOnlineService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoOnlineService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
