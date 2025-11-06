package com.example.simplemvc.service;

import com.example.simplemvc.dto.CategoriaDto;
import com.example.simplemvc.model.Categoria;
import com.example.simplemvc.model.CategoriaMapper;
import com.example.simplemvc.model.enums.EstadoEntidad;
import com.example.simplemvc.repository.CategoriaRepository;
import com.example.simplemvc.request.CrearCategoriaRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public List<CategoriaDto> findAll() {
        log.info("Obteniendo todas las categorias");
        return categoriaRepository.findAll().stream()
            .map(categoriaMapper::toDto)
            .collect(Collectors.toList());
    }
    public CategoriaDto findById(Long id) {
        log.info("Obteniendo categoria con ID: {}", id);
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada con ID: " + id));
        return categoriaMapper.toDto(categoria);
    }


    public CategoriaDto create(CrearCategoriaRequest request) {
        log.info("Creando nueva categoria");
        Optional<Categoria> existingCategoria = categoriaRepository.findByNombre(request.getNombre());
        if (existingCategoria.isPresent()) {
            throw new IllegalArgumentException("Ya existe una categoria con el nombre: " + request.getNombre());
        }
        Categoria categoria = Categoria.builder()
            .nombre(request.getNombre())
            .descripcion(request.getDescripcion())
            .estado(EstadoEntidad.ACTIVO)
            .build();
        Categoria nueva = categoriaRepository.save(categoria);
        return categoriaMapper.toDto(nueva);
    }
}
