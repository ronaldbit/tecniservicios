package com.example.simplemvc.service;

import com.example.simplemvc.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
