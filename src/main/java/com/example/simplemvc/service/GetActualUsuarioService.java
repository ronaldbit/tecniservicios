package com.example.simplemvc.service;

import org.springframework.stereotype.Service;

import com.example.simplemvc.model.Usuario;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public final class GetActualUsuarioService {

  private final GetCurrentUsuarioService getCurrentUsuarioService;

  private final UsuarioService usuarioService;

  public Usuario get() {
    Usuario usuario = getCurrentUsuarioService.get();

    if (usuario == null) {
      return null;
    }

    return usuarioService.obtenerEntidadPorId(usuario.getId()).orElse(null);
  }
}
