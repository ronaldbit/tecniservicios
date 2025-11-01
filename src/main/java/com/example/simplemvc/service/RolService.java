package com.example.simplemvc.service;

import com.example.simplemvc.dto.RolDto;
import com.example.simplemvc.model.Rol;
import com.example.simplemvc.model.RolMapper;
import com.example.simplemvc.repository.RolRepository;
import com.example.simplemvc.repository.UsuarioRepository;
import com.example.simplemvc.request.CrearUsuarioRol;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RolService {
  private final RolRepository rolRepository;
  private final PermisoService permisoService;
  private final UsuarioRepository usuarioRepository;
  private final RolMapper rolMapper;

  public List<RolDto> lista() {
    log.info("Obteniendo lista de roles");
    List<Rol> roles = rolRepository.findAll().stream().filter(Rol::isEstado).toList();
    return roles.stream().map(rolMapper::toDto).toList();
  }

  public RolDto obtenerPorId(Long id) {
    log.info("Obteniendo rol con ID: {}", id);
    Rol rol = rolRepository
        .findById(id).stream().filter(Rol::isEstado).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con ID: " + id));
    return rolMapper.toDto(rol);
  }

  public RolDto actualizar(Long id, CrearUsuarioRol request) {
    log.info("Actualizando rol con ID: {}", id);
    Rol rolExistente = rolRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con ID: " + id));
    rolExistente.setNombre(request.getNombre());
    rolExistente.setDescripcion(request.getDescripcion());
    rolExistente.setPermisos(permisoService.ActualizarPermisoRol(id, request.getPermisos()));
    Rol updated = rolRepository.save(rolExistente);
    log.info("Rol actualizado con ID: {}", id);
    return rolMapper.toDto(updated);
  }

  public RolDto crear(CrearUsuarioRol request) {
    log.info("Creando nuevo rol");
    Optional<Rol> existente = rolRepository.findByNombre(request.getNombre());
    if (existente.isPresent()) {
      Rol rolExistente = existente.get();
      if (rolExistente.isEstado()) {
        throw new IllegalArgumentException("El rol ya existe con el nombre: " + request.getNombre());
      } else {    
        log.info("Reactivando rol inactivo con nombre: {}", request.getNombre());
        rolExistente.setEstado(true);
        rolExistente.setDescripcion(request.getDescripcion());
        rolExistente.setPermisos(permisoService.ActualizarPermisoRol(rolExistente.getId(), request.getPermisos()));
        Rol reactivado = rolRepository.save(rolExistente);
        return rolMapper.toDto(reactivado);
      }
    }
    Rol rol = rolMapper.fromRequest(request).build();
    rol.setEstado(true);
    Rol saved = rolRepository.save(rol);
    request
        .getPermisos()
        .forEach(
            permisoRequest -> {
              permisoRequest.setRolId(saved.getId());
              permisoService.crearPermiso(permisoRequest);
            });
    log.info("Rol creado con ID: {}", saved.getId());
    return rolMapper.toDto(saved);
  }

  public void eliminarPorId(Long id) {
    log.info("Eliminando rol con ID: {}", id);
    usuarioRepository.findByRoles_Id(id).ifPresent(usuario -> {
      throw new IllegalArgumentException("No se puede eliminar el rol, está asignado a un usuario.");
    });
    rolRepository.findById(id).ifPresent(rol -> {
      rol.setEstado(false);
      rolRepository.save(rol);
    });
    log.info("Rol eliminado con ID: {}", id);
  }

  public Rol obtenerEntidadPorId(Long id) {
    log.info("Obteniendo rol con ID: {}", id);
    return rolRepository.findById(id).orElse(null);
  }
}
