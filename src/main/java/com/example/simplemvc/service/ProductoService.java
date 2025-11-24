package com.example.simplemvc.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.simplemvc.model.enums.EstadoEntidad;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.simplemvc.dto.ProductoDto;
import com.example.simplemvc.model.Categoria;
import com.example.simplemvc.model.Marca;
import com.example.simplemvc.model.Producto;
import com.example.simplemvc.model.ProductoMapper;
import com.example.simplemvc.repository.CategoriaRepository;
import com.example.simplemvc.repository.MarcaRepository;
import com.example.simplemvc.repository.ProductoRepository;
import com.example.simplemvc.request.ActualizarInventarioRequest;
import com.example.simplemvc.request.CrearProductoRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Value("${spring.image.path}")
    private String uploadPath;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
      
    @Transactional
    public ProductoDto create(CrearProductoRequest request, List<String> nombresImagenes)
            throws JsonProcessingException {
        String imagenesJson = "[]";
        if (nombresImagenes != null && !nombresImagenes.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            imagenesJson = mapper.writeValueAsString(nombresImagenes);
        }
        System.out.println(" este es: " + request.getCodigo() + " codigo recibido");
        if (request.getCodigo() != null) {
            Optional<Producto> existingProductoOpt = productoRepository.findByCodigo(request.getCodigo());
            if (existingProductoOpt.isPresent()) {
                Producto existingProducto = existingProductoOpt.get();
                if (existingProducto.getEstado() == EstadoEntidad.ELIMINADO) {
                    productoMapper.updateEntityFromRequest(existingProducto, request);
                    existingProducto.setEstado(EstadoEntidad.ACTIVO);
                    existingProducto.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
                    existingProducto.setImagenes(imagenesJson);
                    Producto reactivated = productoRepository.save(existingProducto);
                    log.info("Producto reactivado con ID: {}", reactivated.getIdProducto());
                    return productoMapper.toDto(reactivated);
                }
                throw new IllegalArgumentException("Ya existe un producto con el código: " +
                        request.getCodigo());
            }
        } else {
            request.setCodigo(String.valueOf(System.currentTimeMillis()));
        }
        Producto producto = productoMapper.toEntity(request);
        producto.setImagenes(imagenesJson);
        producto.setEstado(EstadoEntidad.ACTIVO);
        producto.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        Producto saved = productoRepository.save(producto);
        return productoMapper.toDto(saved);
    }

    @Transactional
    public ProductoDto update(Long id, CrearProductoRequest request, List<String> nombresNuevosGuardados) {
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
        List<String> imagenesActualesDB = new ArrayList<>();
        try {
            if (existingProducto.getImagenes() != null && !existingProducto.getImagenes().equals("[]")) {
                imagenesActualesDB = objectMapper.readValue(existingProducto.getImagenes(),
                        new TypeReference<List<String>>() {
                        });
            }
        } catch (Exception e) {
            log.warn("Error al parsear JSON de imágenes actuales del producto {}: {}", id, e.getMessage());
        }       
        List<String> imagenesExistentesAConservar = new ArrayList<>();
        if (request.getImagenesExistentes() != null && !request.getImagenesExistentes().isEmpty()) {
            imagenesExistentesAConservar = Arrays.asList(request.getImagenesExistentes().split(","));
        }       
        List<String> imagenesAEliminar = new ArrayList<>(imagenesActualesDB);
        imagenesAEliminar.removeAll(imagenesExistentesAConservar);
        for (String nombreImagen : imagenesAEliminar) {
            try {
                Path path = Paths.get(uploadPath).resolve(nombreImagen);
                Files.deleteIfExists(path);
                log.info("Imagen eliminada del disco: {}", path.toString());
            } catch (Exception e) {
                log.warn("No se pudo eliminar la imagen del disco: {}. Causa: {}", nombreImagen, e.getMessage());
            }
        }       
        List<String> listaFinalDeImagenes = new ArrayList<>(imagenesExistentesAConservar);
        listaFinalDeImagenes.addAll(nombresNuevosGuardados);
        String imagenesJson = "[]";
        try {
            if (!listaFinalDeImagenes.isEmpty()) {
                imagenesJson = objectMapper.writeValueAsString(listaFinalDeImagenes);
            }
        } catch (Exception e) {
            log.warn("Error al crear JSON de imágenes finales: {}", e.getMessage());
        }       
        existingProducto.setNombre(request.getNombre());
        existingProducto.setPrecio(request.getPrecio());
        existingProducto.setUnidad(request.getUnidad());
        existingProducto.setStockMinimo(request.getStockMinimo());
        existingProducto.setMarca(marca);
        existingProducto.setDescripcion(request.getDescripcion());
        existingProducto.setPrecioOnline(request.getPrecioOnline());
        existingProducto.setDestacado(request.getDestacado());
        existingProducto.setCategoria(categoria);
        existingProducto.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        existingProducto.setImagenes(imagenesJson);
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

    @Transactional
    public void actualizarInventario(ActualizarInventarioRequest request) {
        Producto producto = productoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con ID: " + request.getIdProducto()));

        if (request.getModo().equalsIgnoreCase("AUMENTAR")) {
            producto.setStockActual(producto.getStockActual().add(
                    java.math.BigDecimal.valueOf(request.getCantidad())));
        } else if (request.getModo().equalsIgnoreCase("QUITAR")) {
            if (producto.getStockActual().compareTo(
                    java.math.BigDecimal.valueOf(request.getCantidad())) < 0) {
                throw new IllegalArgumentException("No se puede quitar más stock del que hay disponible.");
            } else {
                producto.setStockActual(producto.getStockActual().subtract(
                        java.math.BigDecimal.valueOf(request.getCantidad())));
            }
        } else {
            throw new IllegalArgumentException("Modo inválido. Use 'AUMENTAR' o 'QUITAR'.");
        }

        producto.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        productoRepository.save(producto);
        log.info("Inventario actualizado para producto ID: {}", request.getIdProducto());

    }

    public void actualizarEstado(Long id) {
        log.info("Actualizando estado del producto con ID: {}", id);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));

        if (producto.getEstado() == EstadoEntidad.ACTIVO) {
            producto.setEstado(EstadoEntidad.INACTIVO);
        } else if (producto.getEstado() == EstadoEntidad.INACTIVO) {
            producto.setEstado(EstadoEntidad.ACTIVO);
        } else {
            throw new IllegalArgumentException("No se puede actualizar el estado de un producto eliminado.");
        }

        producto.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        productoRepository.save(producto);
        log.info("Estado del producto actualizado a {} para el producto ID: {}", producto.getEstado(), id);
    }

}
