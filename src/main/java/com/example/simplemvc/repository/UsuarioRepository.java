package com.example.simplemvc.repository;

import com.example.simplemvc.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  Optional<Usuario> findByNombreUsuario(String nombreUsuario);
  Optional<Usuario> findByPersona_Id(Long idPersona);
  Optional<Usuario> findByRoles_Id(Long idRol);
}
