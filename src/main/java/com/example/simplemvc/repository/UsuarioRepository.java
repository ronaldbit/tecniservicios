package com.example.simplemvc.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simplemvc.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  Optional<Usuario> findByNombreUsuario(String nombreUsuario);
  Optional<Usuario> findByPersona_Id(Long idPersona);
  Optional<Usuario> findByRoles_Id(Long idRol);
  Optional<Usuario> findByJwtPSecret(String jwtPSecret);
  Optional<Usuario> findByPersonaNumeroDocumentoOrPersonaEmail(String numeroDocumento, String email);
}
