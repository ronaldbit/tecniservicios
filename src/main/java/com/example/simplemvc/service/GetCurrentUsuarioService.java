package com.example.simplemvc.service;

import com.example.simplemvc.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class GetCurrentUsuarioService {
  public Usuario get() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null) {
      return null;
    }

    Object user = authentication.getPrincipal();

    if (user instanceof Usuario) {
      return (Usuario) user;
    }

    return null;
  }
}
