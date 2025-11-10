package com.example.simplemvc.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.simplemvc.dto.ProveedorDto;
import com.example.simplemvc.model.Proveedor;
import com.example.simplemvc.model.ProveedorMapper;
import com.example.simplemvc.model.enums.EstadoEntidad;
import com.example.simplemvc.repository.ProveedorRepository;
import com.example.simplemvc.request.CrearProveedorRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;

    public List<ProveedorDto> Listar() {
        log.info("Obteniendo todos los proveedores");
        return proveedorRepository.findAll().stream().filter(p -> p.getEstado() == EstadoEntidad.ACTIVO)
                .map(proveedorMapper::toDto)
                .toList();
    }

    public ProveedorDto crear(CrearProveedorRequest request) {
        proveedorRepository.findByRuc(request.getRuc()).ifPresent(p -> {
            throw new IllegalArgumentException("Ya existe un proveedor con el RUC: " + request.getRuc());
        });
        Proveedor proveedor = proveedorMapper.toEntity(request);
        proveedor.setEstado(EstadoEntidad.ACTIVO);
        Proveedor saved = proveedorRepository.save(proveedor);
        return proveedorMapper.toDto(saved);
    }

    public ProveedorDto actualizar(Long id, CrearProveedorRequest request) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

        proveedorRepository.findByRuc(request.getRuc()).ifPresent(p -> {
            if (!p.getId().equals(id)) {
                throw new IllegalArgumentException("Ya existe un proveedor con el RUC: " + request.getRuc());
            }
        });

        proveedorMapper.updateEntityFromRequest(proveedor, request);
        return proveedorMapper.toDto(proveedorRepository.save(proveedor));
    }

    public void eliminar(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
        proveedor.setEstado(EstadoEntidad.ELIMINADO);
        proveedorRepository.save(proveedor);
    }
}
