package com.example.simplemvc.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.simplemvc.dto.MarcaDto;
import com.example.simplemvc.model.Marca;
import com.example.simplemvc.model.MarcaMapper;
import com.example.simplemvc.model.enums.EstadoEntidad;
import com.example.simplemvc.repository.MarcaRepository;
import com.example.simplemvc.request.CrearMarcaRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MarcaService {
    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    public List<MarcaDto> findAll() {
        log.info("Obteniendo todas las marcas");
        return marcaRepository.findAll().stream()
                .filter(m -> m.getEstado() != EstadoEntidad.ELIMINADO)
                .map(marcaMapper::toDto)
                .collect(Collectors.toList());
    }

    public MarcaDto ActualizarEstado(Long id) {
        log.info("Actualizando estado de la marca con ID: {}", id);
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Marca no encontrada con ID: " + id));
        if (marca.getEstado() == EstadoEntidad.ACTIVO) {
            marca.setEstado(EstadoEntidad.INACTIVO);
        } else if (marca.getEstado() == EstadoEntidad.INACTIVO) {
            marca.setEstado(EstadoEntidad.ACTIVO);
        } else {
            throw new IllegalStateException("No se puede actualizar el estado de una marca eliminada.");
        }
        Marca updatedMarca = marcaRepository.save(marca);
        log.info("Estado de la marca con ID: {} actualizado a {}", id, updatedMarca.getEstado());
        return marcaMapper.toDto(updatedMarca);
    }
    
    public MarcaDto findById(Long id) {
        log.info("Obteniendo marca con ID: {}", id);
        return marcaRepository.findById(id)
                .map(marcaMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Marca no encontrada con ID: " + id));
    }
    public MarcaDto create(CrearMarcaRequest request) {
        log.info("Creando nueva marca");
        Optional<Marca> existingMarca = marcaRepository.findByNombre(request.getNombre());
        if (existingMarca.isPresent()) {
            Marca marca = existingMarca.get();
            if (marca.getEstado() == EstadoEntidad.ELIMINADO) {
                log.info("Reactivando marca eliminada con ID: {}", marca.getId());
                marca.setEstado(EstadoEntidad.ACTIVO);
                Marca reactivatedMarca = marcaRepository.save(marca);
                return marcaMapper.toDto(reactivatedMarca);
            } else {
                throw new IllegalArgumentException("Ya existe una marca con el nombre: " + request.getNombre());
            }

        }
        Marca marca = marcaMapper.toEntity(request);
        Marca savedMarca = marcaRepository.save(marca);
        log.info("Marca creada con ID: {}", savedMarca.getId());
        return marcaMapper.toDto(savedMarca);
    }
    public MarcaDto update(Long id, CrearMarcaRequest request) {
        log.info("Actualizando marca con ID: {}", id);
        Optional<Marca> existingMarca = marcaRepository.findByNombre(request.getNombre());
        if (existingMarca.isPresent()) {
            Marca marca = existingMarca.get();
            if (!marca.getId().equals(id) && marca.getEstado() != EstadoEntidad.ELIMINADO) {
                throw new IllegalArgumentException("Ya existe una marca con el nombre: " + request.getNombre());
            }
        }
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Marca no encontrada con ID: " + id));
        marca.setNombre(request.getNombre());
        marca.setDescripcion(request.getDescripcion());
        Marca updatedMarca = marcaRepository.save(marca);
        log.info("Marca actualizada con ID: {}", updatedMarca.getId());
        return marcaMapper.toDto(updatedMarca);
    }
    public void delete(Long id) {
        log.info("Eliminando marca con ID: {}", id);
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Marca no encontrada con ID: " + id));
        marca.setEstado(EstadoEntidad.ELIMINADO);
        marcaRepository.save(marca);
    }

}
