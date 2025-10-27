package com.example.simplemvc.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.simplemvc.dto.RolDto;
import com.example.simplemvc.model.Rol;
import com.example.simplemvc.model.RolMapper;
import com.example.simplemvc.repository.RolRepository;
import com.example.simplemvc.request.CrearUsuarioRol;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RolService {
  private final RolRepository rolRepository;
  private final RolMapper rolMapper;

  public List<RolDto> lista() {
    log.info("Obteniendo lista de roles");
    List<Rol> roles = rolRepository.findAll();

    return roles.stream()
        .map(rolMapper::toDto)
        .toList();
  }

  public RolDto obtenerPorId(Long id) {
    log.info("Obteniendo rol con ID: {}", id);
    Rol rol = rolRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con ID: " + id));

    return rolMapper.toDto(rol);
  }

  public RolDto actualizar(Long id, CrearUsuarioRol request) {
    log.info("Actualizando rol con ID: {}", id);
    Rol rolExistente = rolRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con ID: " + id));

    rolExistente.setNombre(request.getNombre());
    Rol updated = rolRepository.save(rolExistente);
    log.info("Rol actualizado con ID: {}", id);
    return rolMapper.toDto(updated);
  }

  public RolDto crear(CrearUsuarioRol request) {
    log.info("Creando nuevo rol");
    Rol rol = rolMapper.fromRequest(request).build();
    Rol saved = rolRepository.save(rol);
    log.info("Rol creado con ID: {}", saved.getId());
    return rolMapper.toDto(saved);
  }

  public void eliminarPorId(Long id) {
    log.info("Eliminando rol con ID: {}", id);
    rolRepository.deleteById(id);
    log.info("Rol eliminado con ID: {}", id);
  }

  public Rol obtenerEntidadPorId(Long id) {
    log.info("Obteniendo rol con ID: {}", id);
    return rolRepository.findById(id).orElse(null);
  }
}
