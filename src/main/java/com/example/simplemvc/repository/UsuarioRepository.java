package com.example.simplemvc.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simplemvc.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
  Optional<Usuario> findByCorreo(String correo);
}
