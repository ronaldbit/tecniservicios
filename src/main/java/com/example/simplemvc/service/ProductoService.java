package com.example.simplemvc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.simplemvc.dto.ProductoDto;
import com.example.simplemvc.model.Producto;
import com.example.simplemvc.model.ProductoMapper;
import com.example.simplemvc.repository.ProductoRepository;
import com.example.simplemvc.request.CrearProductoRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    

    public List<ProductoDto> findAll() {
        log.info("Obteniendo todos los productos");
        return productoRepository.findAll().stream()
            .map(productoMapper::toDto)
            .collect(Collectors.toList());
    }

    public ProductoDto findById(Long id) {
        log.info("Obteniendo producto con ID: {}", id);
        return productoRepository.findById(id)
            .map(productoMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }

    public ProductoDto create(CrearProductoRequest request) {
        log.info("Creando nuevo producto");
        Producto producto = productoMapper.toEntity(request);
        Producto savedProducto = productoRepository.save(producto);
        log.info("Producto creado con ID: {}", savedProducto.getIdProducto());
        return productoMapper.toDto(savedProducto);
    }
    
    public ProductoDto update(Long id, CrearProductoRequest request) {
        log.info("Actualizando producto con ID: {}", id);
        Producto existingProducto = productoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
        existingProducto = productoMapper.toEntity(request);
        existingProducto.setIdProducto(id);
        Producto updatedProducto = productoRepository.save(existingProducto);
        log.info("Producto actualizado con ID: {}", updatedProducto.getIdProducto());
        return productoMapper.toDto(updatedProducto);
    }
}
