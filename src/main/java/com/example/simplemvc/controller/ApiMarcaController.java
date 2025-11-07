package com.example.simplemvc.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.simplemvc.dto.MarcaDto;
import com.example.simplemvc.request.CrearMarcaRequest;
import com.example.simplemvc.service.MarcaService;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/marcas")
@AllArgsConstructor
public class ApiMarcaController {
    private final MarcaService marcaService;
    @GetMapping
    public ResponseEntity<?> lista() {
        try {
            List<MarcaDto> marcas = marcaService.findAll();
            return ResponseEntity.ok(marcas);
        } catch (Exception e) {
            return ResponseEntity.status(Response.SC_INTERNAL_SERVER_ERROR).body("Error al obtener las marcas: " + e.getMessage());
        }
    }    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody CrearMarcaRequest request) {
        try {
            MarcaDto marcaActualizada = marcaService.update(id, request);
            return ResponseEntity.ok(marcaActualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearMarcaRequest request) {
        try {
            MarcaDto nuevaMarca = marcaService.create(request);
            return ResponseEntity.ok(nuevaMarca);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
        public ResponseEntity<?> ActualizarEstado(@PathVariable Long id) {
        try {
            marcaService.ActualizarEstado(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            marcaService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
