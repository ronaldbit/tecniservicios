package com.example.simplemvc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.simplemvc.dto.TipoDocumentoDto;
import com.example.simplemvc.model.TipoDocumento;
import com.example.simplemvc.model.TipoDocumentoMapper;
import com.example.simplemvc.repository.TipoDocumentoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TipoDocumentoService {
  private final TipoDocumentoRepository tipoDocumentoRepository;
  private final TipoDocumentoMapper tipoDocumentoMapper;

  public List<TipoDocumentoDto> lista() {
    return tipoDocumentoRepository.findAll().stream()
        .map(tipoDocumentoMapper::toDto)
        .toList();
  }

  public Optional<TipoDocumento> obtenerEntidadPorId(Long id) {
    return tipoDocumentoRepository.findById(id);
  }
}
