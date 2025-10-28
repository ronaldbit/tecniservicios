package com.example.simplemvc.service;

import com.example.simplemvc.dto.SucursalDto;
import com.example.simplemvc.model.Sucursal;
import com.example.simplemvc.model.SucursalMapper;
import com.example.simplemvc.repository.SucursalRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SucursalService {
  private final SucursalRepository sucursalRepository;
  private final SucursalMapper sucursalMapper;

  public List<SucursalDto> lista() {
    log.info("Obteniendo lista de sucursales");
    return sucursalRepository.findAll().stream().map(sucursalMapper::toDto).toList();
  }

  public Optional<Sucursal> obtenerEntidadPorId(Long id) {
    log.info("Obteniendo sucursal con ID: {}", id);
    return sucursalRepository.findById(id);
  }
}
