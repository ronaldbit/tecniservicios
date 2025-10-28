package com.example.simplemvc.controller;

import com.example.simplemvc.dto.UsuarioDto;
import com.example.simplemvc.request.CrearUsuarioRequest;
import com.example.simplemvc.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class ApiUsuarioController {
  private final UsuarioService usuarioService;

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public UsuarioDto obtenerPorId(@PathVariable Long id) {
    return usuarioService.obtenerPorId(id);
  }

  @PostMapping
  public ResponseEntity<?> crear(@RequestBody CrearUsuarioRequest request) {
    try {
      UsuarioDto usuarioDto = usuarioService.crear(request);
      return ResponseEntity.ok(usuarioDto);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> actualizar(
      @PathVariable Long id, @RequestBody CrearUsuarioRequest request) {
    try {
      UsuarioDto updatedUsuario = usuarioService.actualizar(id, request);
      return ResponseEntity.ok(updatedUsuario);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
