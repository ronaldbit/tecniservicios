package com.example.simplemvc.controller.usuario;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simplemvc.dto.UsuarioDto;
import com.example.simplemvc.request.CrearUsuarioRequest;
import com.example.simplemvc.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class ApiUsuarioController {
  private final UsuarioService usuarioService;

  @GetMapping
  public List<UsuarioDto> lista() {
    return usuarioService.listaTodos();
  }

  @GetMapping("/{id}")
  public UsuarioDto obtenerPorId(@PathVariable UUID id) {
    return usuarioService.obtenerPorId(id);
  }

  @PostMapping
  public UsuarioDto crear(@RequestBody CrearUsuarioRequest request) {
    return usuarioService.crear(request);
  }
}