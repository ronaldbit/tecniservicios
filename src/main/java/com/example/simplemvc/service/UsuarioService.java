package com.example.simplemvc.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.simplemvc.dto.UsuarioDto;
import com.example.simplemvc.model.Persona;
import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.model.UsuarioMapper;
import com.example.simplemvc.model.UsuarioRol;
import com.example.simplemvc.repository.UsuarioRepository;
import com.example.simplemvc.repository.UsuarioRolRepository;
import com.example.simplemvc.request.CrearUsuarioRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {
  private final UsuarioRepository usuarioRepository;
  private final UsuarioMapper usuarioMapper;
  private final UsuarioRolRepository usuarioRolRepository;

  private final PasswordEncoder passwordEncoder;

  private final PersonaService personaService;

  public List<UsuarioDto> listaTodos() {
    log.info("Obteniendo lista de todos los usuarios");

    List<Usuario> usuarios = usuarioRepository.findAll();

    return usuarios.stream().map(usuarioMapper::toDto).collect(Collectors.toList());
  }

  public UsuarioDto obtenerPorId(Long id) {
    log.info("Obteniendo usuario con ID: {}", id);

    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> {
          log.error("Usuario con ID {} no encontrado.", id);
          return new IllegalArgumentException("Usuario no encontrado.");
        });

    return usuarioMapper.toDto(usuario);
  }

  @Transactional
  public UsuarioDto crear(CrearUsuarioRequest request) {
    log.info("Creando usuario");

    Persona persona = personaService.obtenerEntidadPorId(request.getPersonaId());
    if (persona == null) {
      log.error("No se puede crear el usuario. La persona con ID {} no existe.", request.getPersonaId());

      throw new IllegalArgumentException("La persona asociada no existe.");
    }

    Optional<Usuario> prevUsuario = usuarioRepository.findByCorreo(request.getCorreo());

    if (prevUsuario.isPresent() && prevUsuario.get().isDeleted() == false) {
      log.error("No se puede crear el usuario. El correo {} ya está en uso.", request.getCorreo());

      throw new IllegalArgumentException("El correo ya está en uso.");
    }

    if (prevUsuario.isPresent() && prevUsuario.get().isDeleted() == true) {
      log.info("Restaurando usuario previamente eliminado con correo: {}", request.getCorreo());

      Usuario usuarioEliminado = prevUsuario.get();
      usuarioEliminado.setDeleted(false);
      usuarioEliminado.setPersona(persona);
      usuarioEliminado.setCorreo(request.getCorreo());
      usuarioEliminado.setPassword(passwordEncoder.encode(request.getPassword()));

      usuarioEliminado = usuarioRepository.save(usuarioEliminado);

      log.info("Usuario restaurado con ID: {}", usuarioEliminado.getId());
      return usuarioMapper.toDto(usuarioEliminado);
    }

    UsuarioRol rol = usuarioRolRepository.findByNombre("USER")
        .orElseThrow(() -> {
          log.error("No se puede crear el usuario. El rol USUARIO no existe.");
          return new IllegalArgumentException("El rol especificado no existe.");
        });

    Usuario usuario = usuarioMapper.fromRequest(request).persona(persona).rol(rol)
        .password(passwordEncoder.encode(request.getPassword())).build();

    usuario = usuarioRepository.save(usuario);

    log.info("Usuario creado con ID: {}", usuario.getId());
    return usuarioMapper.toDto(usuario);
  }

  public UsuarioDto actualizar(Long id, CrearUsuarioRequest request) {
    log.info("Actualizando usuario con ID: {}", id);

    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> {
          log.error("Usuario con ID {} no encontrado.", id);
          return new IllegalArgumentException("Usuario no encontrado.");
        });

    usuario.setCorreo(request.getCorreo());
    usuario.setPassword(passwordEncoder.encode(request.getPassword()));

    usuario = usuarioRepository.save(usuario);

    log.info("Usuario actualizado con ID: {}", usuario.getId());
    return usuarioMapper.toDto(usuario);
  }

  public Optional<Usuario> obtenerEntidadPorCorreo(String correo) {
    log.info("Obteniendo usuario con correo: {}", correo);

    return usuarioRepository.findByCorreo(correo);
  }
}
