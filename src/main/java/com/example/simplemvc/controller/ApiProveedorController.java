package com.example.simplemvc.controller;

import com.example.simplemvc.dto.ProveedorDto;
import com.example.simplemvc.request.CrearProveedorRequest;
import com.example.simplemvc.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
public class ApiProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<ProveedorDto> proveedores = proveedorService.Listar();
            return ResponseEntity.ok(proveedores);
        } catch (Exception e) {
            log.error("Error al listar proveedores: {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearProveedorRequest request) {
        try {
            log.info("Creando nuevo proveedor");
            ProveedorDto nuevoProveedor = proveedorService.crear(request);
            return ResponseEntity.ok(nuevoProveedor);
        } catch (Exception e) {
            log.error("Error al crear proveedor: {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());

        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody CrearProveedorRequest request) {
        try {
            log.info("Actualizando proveedor con id {}", id);
            ProveedorDto actualizado = proveedorService.actualizar(id, request);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            log.error("Error al actualizar proveedor: {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        log.info("Eliminando proveedor con id {}", id);
        proveedorService.eliminar(id);
    }
}
