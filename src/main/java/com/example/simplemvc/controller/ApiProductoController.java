package com.example.simplemvc.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.simplemvc.request.ActualizarInventarioRequest;
import com.example.simplemvc.request.CrearProductoRequest;

//import org.apache.tomcat.util.http.parser.MediaType;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.simplemvc.dto.ProductoDto;
import com.example.simplemvc.service.ProductoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;


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
/**
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody CrearProductoRequest request) {
        try {
            ProductoDto nuevoProducto = productoService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    } **/


@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<?> crearProducto(@ModelAttribute CrearProductoRequest request) {
    try {
        List<String> nombresGuardados = new ArrayList<>();

        if (request.getImagenes() != null) {
            for (MultipartFile file : request.getImagenes()) {
                if (!file.isEmpty()) {
                    String nombre = UUID.randomUUID() + "_" + file.getOriginalFilename();

                    Path path = Paths.get("/imgs/productos/" + nombre);
                    Files.createDirectories(path.getParent()); // crea la carpeta si no existe
                    Files.write(path, file.getBytes());


                    nombresGuardados.add(nombre);
                }
            }
        }

        ProductoDto nuevoProducto = productoService.create(request, nombresGuardados);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody CrearProductoRequest request) {
        try {
            ProductoDto productoActualizado = productoService.update(id, request);
            return ResponseEntity.ok(productoActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.delete(id);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDto>> buscarProductos(@RequestParam String query) {
        List<ProductoDto> resultados = productoService.buscarProductos(query);
        return ResponseEntity.ok(resultados);
    }

    @PostMapping("/actualizarInventario")
    public ResponseEntity<?> actualizarInventario(@RequestBody ActualizarInventarioRequest request) {
        try {
            productoService.actualizarInventario(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id) {
        try {
            productoService.actualizarEstado(id);
            return ResponseEntity.ok("Estado actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
