package com.example.simplemvc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.simplemvc.dto.PermisoDto;
import com.example.simplemvc.model.Permiso;
import com.example.simplemvc.model.PermisoMapper;
import com.example.simplemvc.model.Rol;
import com.example.simplemvc.repository.PermisoRepository;
import com.example.simplemvc.repository.RolRepository;
import com.example.simplemvc.request.CrearPermisoRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermisoService {
    private final PermisoRepository permisoRepository;
    private final PermisoMapper permisoMapper;
    private final RolRepository rolRepository;

    public List<PermisoDto> obtenerPermisosPorRolId(Long rolId) {
        log.info("Obteniendo permisos para el rol con ID: {}", rolId);
        List<Permiso> permisos = permisoRepository.findByRolId(rolId);
        return permisos.stream().map(permisoMapper::toDto).collect(Collectors.toList());
    }

    public void actualizarPermiso(Long permisoId, CrearPermisoRequest request) {
        log.info("Actualizando permiso con ID: {}", permisoId);
        Permiso permisoExistente = permisoRepository.findById(permisoId)
                .orElseThrow(() -> new IllegalArgumentException("Permiso no encontrado con ID: " + permisoId));
        permisoExistente.setPath(request.getPath());
        permisoRepository.save(permisoExistente);
        log.info("Permiso actualizado con ID: {}", permisoId);
    }

    public void crearPermiso(CrearPermisoRequest request) {
        log.info("Creando permiso con path: {} para rol ID: {}", request.getPath(), request.getRolId());
        Rol rolAsociado = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con ID: " + request.getRolId()));
        Permiso permiso = permisoMapper.fromRequest(request).build();
        permiso.setRol(rolAsociado);
        permisoRepository.save(permiso); 
        log.info("Permiso guardado correctamente con path: {}", permiso.getPath());
    }

    public void eliminarPermiso(Long permisoId) {
        try {
            log.info("Eliminando permiso con ID: {}", permisoId);
            permisoRepository.deleteById(permisoId);
            log.info("Permiso eliminado con ID: {}", permisoId);
        } catch (Exception e) {
            log.error("Error al eliminar el permiso con ID: {}", permisoId, e);
            throw new IllegalArgumentException("No se pudo eliminar el permiso con ID: " + permisoId);
        }
    }
}