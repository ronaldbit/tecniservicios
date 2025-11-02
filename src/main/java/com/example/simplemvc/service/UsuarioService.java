package com.example.simplemvc.service;

import com.example.simplemvc.dto.UsuarioDto;
import com.example.simplemvc.model.Persona;
import com.example.simplemvc.model.Rol;
import com.example.simplemvc.model.Sucursal;
import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.model.UsuarioMapper;
import com.example.simplemvc.model.enums.EstadoEntidad;
import com.example.simplemvc.repository.RolRepository;
import com.example.simplemvc.repository.UsuarioRepository;
import com.example.simplemvc.request.CrearUsuarioRequest;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {
  private final UsuarioRepository usuarioRepository;
  private final UsuarioMapper usuarioMapper;
  private final RolRepository rolRepository;
  private final SucursalService sucursalService;

  private final PasswordEncoder passwordEncoder;

  private final PersonaService personaService;

  public List<UsuarioDto> listaTodos() {
    log.info("Obteniendo lista de todos los usuarios");

    List<Usuario> usuarios = usuarioRepository.findAll();

    return usuarios.stream().map(usuarioMapper::toDto).collect(Collectors.toList());
  }

  public Optional<Usuario> obtenerEntidadPorId(Long id) {
    log.info("Obteniendo entidad de usuario con ID: {}", id);

    return usuarioRepository.findById(id);
  }

  public UsuarioDto obtenerPorId(Long id) {
    log.info("Obteniendo usuario con ID: {}", id);

    Usuario usuario =
        usuarioRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  log.error("Usuario con ID {} no encontrado.", id);
                  return new IllegalArgumentException("Usuario no encontrado.");
                });

    return usuarioMapper.toDto(usuario);
  }

  @Transactional
  public UsuarioDto crear(CrearUsuarioRequest request) {
    log.info("Creando usuario");
    Persona persona =
        personaService
            .obtenerEntidadPorId(request.getPersonaId())
            .orElseThrow(() -> new IllegalArgumentException("La persona asociada no existe."));
    Sucursal sucursal =
        sucursalService
            .obtenerEntidadPorId(request.getSucursalId())
            .orElseThrow(() -> new IllegalArgumentException("La sucursal asociada no existe."));

    Optional<Usuario> prevUsuario =
        usuarioRepository.findByNombreUsuario(request.getNombreUsuario());

    if (prevUsuario.isPresent() && prevUsuario.get().getEstado() == EstadoEntidad.ACTIVO) {
      log.error(
          "No se puede crear el usuario. El nombre de usuario {} ya está en uso.",
          request.getNombreUsuario());

      throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
    }

    if (prevUsuario.isPresent() && prevUsuario.get().getEstado() == EstadoEntidad.ELIMINADO) {
      log.info(
          "Restaurando usuario previamente eliminado con nombre de usuario: {}",
          request.getNombreUsuario());

      Usuario usuarioEliminado = prevUsuario.get();
      usuarioEliminado.setEstado(EstadoEntidad.ACTIVO);
      usuarioEliminado.setPersona(persona);
      usuarioEliminado.setSucursal(sucursal);
      usuarioEliminado.setNombreUsuario(request.getNombreUsuario());
      usuarioEliminado.setPassword(passwordEncoder.encode(request.getPassword()));

      usuarioEliminado = usuarioRepository.save(usuarioEliminado);

      log.info("Usuario restaurado con ID: {}", usuarioEliminado.getId());
      return usuarioMapper.toDto(usuarioEliminado);
    }

    Rol rol =
        rolRepository
            .findByNombre("CLIENTE")
            .orElseThrow(
                () -> {
                  log.error("No se puede crear el usuario. El rol CLIENTE no existe.");
                  return new IllegalArgumentException("El rol especificado no existe.");
                });

    if (request.getRolId() != null) {
      rol =
          rolRepository
              .findById(request.getRolId())
              .orElseThrow(
                  () -> {
                    log.error(
                        "No se puede crear el usuario. El rol con ID {} no existe.",
                        request.getRolId());
                    return new IllegalArgumentException("El rol especificado no existe.");
                  });
    }
    Usuario usuario =
        usuarioMapper
            .fromRequest(request)
            .persona(persona)
            .sucursal(sucursal)
            .roles(Arrays.asList(rol))
            .password(passwordEncoder.encode(request.getPassword()))
            .build();

    usuario = usuarioRepository.save(usuario);

    log.info("Usuario creado con ID: {}", usuario.getId());
    return usuarioMapper.toDto(usuario);
  }

  public UsuarioDto actualizar(Long id, CrearUsuarioRequest request) {
    log.info("Actualizando usuario con ID: {}", id);

    Rol rol =
        rolRepository
            .findById(request.getRolId())
            .orElseThrow(
                () -> {
                  log.error(
                      "No se puede actualizar el usuario. El rol con ID {} no existe.",
                      request.getRolId());
                  return new IllegalArgumentException("El rol especificado no existe.");
                });

    Usuario usuario =
        usuarioRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  log.error("Usuario con ID {} no encontrado.", id);
                  return new IllegalArgumentException("Usuario no encontrado.");
                });

    System.out.println("Actualizar Usuario ID: " + id + ", Request: " + request);
    usuario.setEstado(
        request.getEstadoEntidad() == 1
            ? EstadoEntidad.INACTIVO
            : request.getEstadoEntidad() == 2 ? EstadoEntidad.ACTIVO : EstadoEntidad.ELIMINADO);

    if (request.getEstadoEntidad() != 1 && request.getEstadoEntidad() != 2 ) {
       log.info("Eliminando persona asociada al usuario con ID: {}", id);       
       Usuario temp = usuarioRepository.findById(id).orElseThrow();
       personaService.eliminarPorId(temp.getPersona().getId());
       log.info("Persona eliminada con ID: {}", temp.getPersona().getId());
    }
    usuario.setNombreUsuario(request.getNombreUsuario());
    if(request.getPassword() != null && !request.getPassword().isEmpty()){
      usuario.setPassword(passwordEncoder.encode(request.getPassword()));
    } else {
      log.info("La contraseña no se actualiza para el usuario con ID: {}", id);
    }    
    usuario.setRoles(Arrays.asList(rol));
    usuario = usuarioRepository.save(usuario);
    log.info("Usuario actualizado con ID: {}", usuario.getId());
    return usuarioMapper.toDto(usuario);
  }

  public Optional<Usuario> obtenerEntidadPorNombreUsuario(String nombreUsuario) {
    log.info("Obteniendo usuario con nombre de usuario: {}", nombreUsuario);

    return usuarioRepository.findByNombreUsuario(nombreUsuario);
  }
}
