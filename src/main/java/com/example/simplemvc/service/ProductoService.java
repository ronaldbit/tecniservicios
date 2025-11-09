package com.example.simplemvc.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.simplemvc.model.enums.EstadoEntidad;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.simplemvc.dto.ProductoDto;
import com.example.simplemvc.model.Categoria;
import com.example.simplemvc.model.Marca;
import com.example.simplemvc.model.Producto;
import com.example.simplemvc.model.ProductoMapper;
import com.example.simplemvc.repository.CategoriaRepository;
import com.example.simplemvc.repository.MarcaRepository;
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
    private final MarcaRepository marcaRepository;
    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<ProductoDto> findAll() {
        log.info("Obteniendo todos los productos");
        return productoRepository.findAll().stream()
                .filter(p -> p.getEstado() != EstadoEntidad.ELIMINADO)
                .map(productoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoDto findById(Long id) {
        log.info("Obteniendo producto con ID: {}", id);
        return productoRepository.findById(id)
                .map(productoMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }

    public ProductoDto create(CrearProductoRequest request) {
        log.info("Creando nuevo producto");
        Optional<Producto> existingProductoOpt = productoRepository.findByCodigo(request.getCodigo());
        if (existingProductoOpt.isPresent()) {
            Producto existingProducto = existingProductoOpt.get();
            if (existingProducto.getEstado() == EstadoEntidad.ELIMINADO) {
                productoMapper.updateEntityFromRequest(existingProducto, request);
                existingProducto.setEstado(EstadoEntidad.ACTIVO);
                existingProducto.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
                Producto reactivated = productoRepository.save(existingProducto);
                log.info("Producto reactivado con ID: {}", reactivated.getIdProducto());
                return productoMapper.toDto(reactivated);
            }
            throw new IllegalArgumentException("Ya existe un producto con el código: " + request.getCodigo());
        }

        Producto nuevoProducto = productoMapper.toEntity(request);
        nuevoProducto.setEstado(EstadoEntidad.ACTIVO);
        nuevoProducto.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        nuevoProducto.setUpdatedAt(null);
        Producto saved = productoRepository.save(nuevoProducto);
        log.info("Producto creado con ID: {}", saved.getIdProducto());

        return productoMapper.toDto(saved);
    }

    @Transactional
    public ProductoDto update(Long id, CrearProductoRequest request) {
        log.info("Actualizando producto con ID: {}", id);

        Producto existingProducto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));

        Optional<Producto> productoWithSameCodigo = productoRepository.findByCodigo(request.getCodigo());
        if (productoWithSameCodigo.isPresent() && !productoWithSameCodigo.get().getIdProducto().equals(id)) {
            throw new IllegalArgumentException("Ya existe un producto con el código: " + request.getCodigo());
        }

        Marca marca = marcaRepository.findById(request.getIdMarca())
                .orElseThrow(() -> new IllegalArgumentException("Marca no encontrada con ID: " + request.getIdMarca()));

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Categoría no encontrada con ID: " + request.getIdCategoria()));

        existingProducto.setNombre(request.getNombre());
        existingProducto.setPrecio(request.getPrecio());
        existingProducto.setUnidad(request.getUnidad());
        existingProducto.setStockMinimo(request.getStockMinimo());
        existingProducto.setMarca(marca);
        existingProducto.setCategoria(categoria);
        existingProducto.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        Producto updated = productoRepository.save(existingProducto);
        log.info("Producto actualizado con ID: {}", updated.getIdProducto());
        return productoMapper.toDto(updated);
    }

    public void delete(Long id) {
        log.info("Eliminando producto con ID: {}", id);
        if (!productoRepository.existsById(id)) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
        }
        Producto producto = productoRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
        producto.setEstado(EstadoEntidad.ELIMINADO);
        productoRepository.save(producto);
        log.info("Producto eliminado con ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<ProductoDto> buscarProductos(String query) {
        return productoRepository.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(query, query)
                .stream()
                .filter(p -> p.getEstado() != EstadoEntidad.ELIMINADO)
                .map(productoMapper::toDto)
                .collect(Collectors.toList());
    }

}
