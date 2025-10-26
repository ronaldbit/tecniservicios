package com.example.simplemvc.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.simplemvc.dto.PermisoDto;
import com.example.simplemvc.dto.UsuarioRolDto;
import com.example.simplemvc.model.UsuarioRol;
import com.example.simplemvc.model.UsuarioRolMapper;
import com.example.simplemvc.repository.UsuarioRolRepository;
import com.example.simplemvc.request.CrearPermisoRequest;
import com.example.simplemvc.request.CrearUsuarioRol;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioRolService {
  private final UsuarioRolRepository usuarioRolRepository;
  private final PermisoService permisoService;
  private final UsuarioRolMapper usuarioRolMapper;

  @Transactional(readOnly = true)
  public List<UsuarioRolDto> listarRoles() {
    log.info("Obteniendo lista de roles de usuario");
    List<UsuarioRol> roles = usuarioRolRepository.findAll();

    return roles.stream().map(usuarioRolMapper::toDto).toList();
  }

  @Transactional(readOnly = true)
  public UsuarioRolDto obtenerPorId(Long id) {
    log.info("Obteniendo rol de usuario con ID: {}", id);
    UsuarioRol usuarioRol = usuarioRolRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Rol de usuario no encontrado con ID: " + id));
    return usuarioRolMapper.toDto(usuarioRol);
  }

  public UsuarioRolDto crear(CrearUsuarioRol request) {
    List<CrearPermisoRequest> permisosRequest = request.getPermisos();
    request.setPermisos(null);
    log.info("Creando nuevo rol de usuario con nombre: {}", request.getNombre());
    UsuarioRol usuarioRol = usuarioRolMapper.fromRequest(request).build();
    UsuarioRol savedRol = usuarioRolRepository.save(usuarioRol);
    log.info("Rol de usuario creado con ID: {}", savedRol.getId());
    if (permisosRequest != null) {
      log.info("Creando permisos asociados al rol de usuario con ID: {}", savedRol.getId());
      permisosRequest.forEach(permisoRequest -> {
        permisoRequest.setRolId(savedRol.getId());
        permisoService.crearPermiso(permisoRequest);
      });
    }
    return usuarioRolMapper.toDto(savedRol);
  }

  public void eliminarPorId(Long id) {
    log.info("Eliminando rol de usuario con ID: {}", id);
    usuarioRolRepository.deleteById(id);
    log.info("Rol de usuario eliminado con ID: {}", id);
  }

  public UsuarioRolDto actualizar(Long id, CrearUsuarioRol request) {
    log.info("Actualizando rol de usuario con ID: {}", id);
    UsuarioRol rolExistente = usuarioRolRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Rol de usuario no encontrado con ID: " + id));
    rolExistente.setNombre(request.getNombre());
    UsuarioRol updatedRol = usuarioRolRepository.save(rolExistente);

    if (request.getPermisos() != null) {
      log.info("Actualizando permisos asociados al rol de usuario con ID: {}", updatedRol.getId());

      List<PermisoDto> permisosExistentes = permisoService.obtenerPermisosPorRolId(updatedRol.getId());
      log.info("Permisos existentes encontrados: {}", permisosExistentes.size());
      permisosExistentes.forEach(permiso -> {
        permisoService.eliminarPermiso(permiso.getId());
      });
      log.info("Permisos existentes eliminados para el rol de usuario con ID: {}", updatedRol.getId());
      log.info("Creando nuevos permisos para el rol de usuario con ID: {}", updatedRol.getId());
      request.getPermisos().forEach(permisoRequest -> {
        permisoRequest.setRolId(updatedRol.getId());
        permisoService.crearPermiso(permisoRequest);
      });
    }
    return usuarioRolMapper.toDto(updatedRol);
  }

}
