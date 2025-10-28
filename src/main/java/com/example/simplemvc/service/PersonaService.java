package com.example.simplemvc.service;

import com.example.simplemvc.dto.PersonaDto;
import com.example.simplemvc.model.Persona;
import com.example.simplemvc.model.PersonaMapper;
import com.example.simplemvc.model.TipoDocumento;
import com.example.simplemvc.repository.PersonaRepository;
import com.example.simplemvc.request.CrearPersonaRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonaService {
  private final PersonaRepository personaRepository;
  private final PersonaMapper personaMapper;
  private final TipoDocumentoService tipoDocumentoService;

  public List<PersonaDto> listaTodos() {
    log.info("Obteniendo lista de personas");
    List<Persona> personas = personaRepository.findAll();
    return personas.stream().map(personaMapper::toDto).collect(Collectors.toList());
  }

  public PersonaDto obtenerPorId(Long id) {
    log.info("Obteniendo persona con ID: {}", id);
    Persona persona =
        personaRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + id));

    return personaMapper.toDto(persona);
  }

  public PersonaDto crear(CrearPersonaRequest request) {
    log.info("Creando nueva persona");
    
    TipoDocumento tipoDocumentoDto =
        tipoDocumentoService
            .obtenerEntidadPorId(request.getTipoDocumentoId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Tipo de documento no encontrado con ID: " + request.getTipoDocumentoId()));

    Optional<Persona> personaOpt = personaRepository.findByNumeroDocumento(request.getNumeroDocumento());

    if (personaOpt.isPresent()) {
      log.warn("Ya existe una persona con el número de documento: {}", request.getNumeroDocumento());
      throw new IllegalArgumentException("Ya existe una persona con el número de documento: " + request.getNumeroDocumento());
    }

    Persona persona = personaMapper.fromRequest(request).tipoDocumento(tipoDocumentoDto).build();
    Persona saved = personaRepository.save(persona);
    log.info("Persona creada con ID: {}", saved.getId());
    return personaMapper.toDto(saved);
  }

  public void eliminarPorId(Long id) {
    log.info("Eliminando persona con ID: {}", id);
    personaRepository.deleteById(id);
    log.info("Persona eliminada con ID: {}", id);
  }

  public Optional<Persona> obtenerEntidadPorId(Long id) {
    log.info("Obteniendo persona con ID: {}", id);
    return personaRepository.findById(id);
  }

  public PersonaDto actualizar(Long id, CrearPersonaRequest request) {
    log.info("Actualizando persona con ID: {}", id);
    Persona personaExistente =
        personaRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + id));

    Persona.PersonaBuilder personaBuilder = personaMapper.fromRequest(request);

    Persona personaActualizada;
    personaActualizada =
        personaBuilder
            .id(personaExistente.getId())
            .tipoDocumento(personaExistente.getTipoDocumento())
            .build();

    personaActualizada = personaRepository.save(personaActualizada);

    log.info("Persona actualizada con ID: {}", personaActualizada.getId());
    return personaMapper.toDto(personaActualizada);
  }
}
