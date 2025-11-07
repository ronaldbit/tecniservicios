package com.example.simplemvc.controller;

import java.util.List;

import com.example.simplemvc.request.CrearProductoRequest;
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
    public List<ProductoDto> listarProductos() {
        return productoService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProductoDto crearProducto(@RequestBody CrearProductoRequest request) {
        try {
            return productoService.create(request);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al crear el producto: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductoDto actualizarProducto(@PathVariable Long id, @RequestBody CrearProductoRequest request) {
        try {
            return productoService.update(id, request);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al actualizar el producto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.delete(id);
    }
}
