package com.example.simplemvc.controller;

import com.example.simplemvc.dto.CategoriaDto;
import com.example.simplemvc.request.CrearCategoriaRequest;
import com.example.simplemvc.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class ApiCategoriaController {
    @Autowired
    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearCategoriaRequest request) {
        try {
            CategoriaDto nuevaCategoria = categoriaService.create(request);
            return ResponseEntity.ok(nuevaCategoria);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody CrearCategoriaRequest request) {
        try {
            CategoriaDto categoriaActualizada = categoriaService.update(id, request);
            return ResponseEntity.ok(categoriaActualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        categoriaService.delete(id);
    }

    @GetMapping
    public List<CategoriaDto> lista() {
        return categoriaService.findAll();
    }
}
