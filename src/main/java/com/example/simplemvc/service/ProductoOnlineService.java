package com.example.simplemvc.service;

import com.example.simplemvc.dto.ProductoOnlineDto;
import com.example.simplemvc.model.ProductoOnline;
import com.example.simplemvc.model.Producto;
import com.example.simplemvc.repository.ProductoOnlineRepository;
import com.example.simplemvc.repository.ProductoRepository;
import com.example.simplemvc.request.CrearProductoOnlineRequest;
import com.example.simplemvc.model.ProductoOnlineMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoOnlineService {

    private final ProductoOnlineRepository productoOnlineRepository;
    private final ProductoRepository productoRepository;
    private final ProductoOnlineMapper productoOnlineMapper;

    public List<ProductoOnlineDto> listar() {
        return productoOnlineRepository.findAll()
                .stream()
                .map(productoOnlineMapper::toDto)
                .toList();
    }

    public ProductoOnlineDto obtenerPorId(Long id) {
        ProductoOnline productoOnline = productoOnlineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto online no encontrado con ID: " + id));
        return productoOnlineMapper.toDto(productoOnline);
    }

    public ProductoOnlineDto crear(CrearProductoOnlineRequest request) {
        log.info("Creando producto online para producto ID {}", request.getIdProducto());

        Producto producto = productoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con ID: " + request.getIdProducto()));

        Optional<ProductoOnline> existente = productoOnlineRepository
                .findByProducto_IdProducto(request.getIdProducto());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un registro online para este producto.");
        }

        ProductoOnline entity = productoOnlineMapper.toEntity(request);
        entity.setProducto(producto);
        entity.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        entity.setUpdatedAt(null);

        ProductoOnline saved = productoOnlineRepository.save(entity);
        log.info("ProductoOnline creado con ID: {}", saved.getIdProductoOnline());

        return productoOnlineMapper.toDto(saved);
    }

    public ProductoOnlineDto actualizar(Long id, CrearProductoOnlineRequest request) {
        log.info("Actualizando producto online con ID: {}", id);

        ProductoOnline productoOnline = productoOnlineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto online no encontrado."));

        Producto producto = productoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con ID: " + request.getIdProducto()));

        productoOnlineMapper.updateEntityFromRequest(productoOnline, request);
        productoOnline.setProducto(producto);
        productoOnline.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        ProductoOnline actualizado = productoOnlineRepository.save(productoOnline);
        return productoOnlineMapper.toDto(actualizado);
    }

    public void eliminar(Long id) {
        log.info("Eliminando producto online con ID: {}", id);
        if (!productoOnlineRepository.existsById(id)) {
            throw new IllegalArgumentException("Producto online no encontrado.");
        }
        productoOnlineRepository.deleteById(id);
    }
}
