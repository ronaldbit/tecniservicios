package com.example.simplemvc.controller;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.simplemvc.dto.ProductoDto;
import com.example.simplemvc.request.ActualizarInventarioRequest;
import com.example.simplemvc.request.CrearProductoRequest;
import com.example.simplemvc.service.ProductoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Slf4j
public class ApiProductoController {
  private final ProductoService productoService;

  @Value("${spring.image.path}")
  private String uploadPath;

  @GetMapping
  public ResponseEntity<?> listarProductos(
      @RequestParam(required = false) Long categoriaId,
      @RequestParam(required = false) Long marcaId,
      @RequestParam(required = false) String estado) {
    try {
      List<ProductoDto> productos = productoService.listarConFiltros(categoriaId, marcaId, estado);
      return ResponseEntity.ok(productos);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
  /*
   * public ResponseEntity<List<ProductoDto>> listarProductos() {
   * List<ProductoDto> productos = productoService.findAll();
   * return ResponseEntity.ok(productos);
   * }
   */

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> crearProducto(@ModelAttribute CrearProductoRequest request) {
    try {
      List<String> nombresGuardados = new ArrayList<>();

      if (request.getImagenes() != null) {
        for (MultipartFile file : request.getImagenes()) {
          if (!file.isEmpty()) {
            String nombre = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadPath).resolve(nombre);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            System.out.println();
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

  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> actualizarProducto(@PathVariable Long id,
      @ModelAttribute CrearProductoRequest request) {
    try {
      Set<String> hashesVistos = new HashSet<>();
      List<String> nombresNuevosGuardados = new ArrayList<>();
      if (request.getImagenes() != null) {
        for (MultipartFile file : request.getImagenes()) {
          if (!file.isEmpty()) {
            byte[] fileBytes = file.getBytes();
            String fileHash = getFileHash(fileBytes);
            if (hashesVistos.contains(fileHash)) {
              log.warn("Archivo duplicado detectado (hash: {}). Omitiendo.", fileHash);
              continue;
            }
            hashesVistos.add(fileHash);
            String nombreOriginal = file.getOriginalFilename();
            String nombreLimpio = nombreOriginal.replaceAll("\\s+", "_");
            String nombre = UUID.randomUUID() + "_" + nombreLimpio;

            Path path = Paths.get(uploadPath).resolve(nombre);
            Files.createDirectories(path.getParent());
            Files.write(path, fileBytes);

            nombresNuevosGuardados.add(nombre);
          }
        }
      }
      ProductoDto productoActualizado = productoService.update(id, request, nombresNuevosGuardados);
      return ResponseEntity.ok(productoActualizado);

    } catch (Exception e) {
      log.error("Error al actualizar el producto con ID {}: {}", id, e.getMessage(), e);
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

  private String getFileHash(byte[] bytes) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] digest = md.digest(bytes);
      return new BigInteger(1, digest).toString(16);
    } catch (NoSuchAlgorithmException e) {
      log.error("Error: No se encontró el algoritmo MD5", e);
      return UUID.randomUUID().toString();
    }
  }
}
