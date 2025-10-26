package com.example.simplemvc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.simplemvc.dto.PersonaDto;
import com.example.simplemvc.model.Persona;
import com.example.simplemvc.model.PersonaMapper;
import com.example.simplemvc.repository.PersonaRepository;
import com.example.simplemvc.request.CrearPersonaRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonaService {
  private final PersonaRepository personaRepository;
  private final PersonaMapper personaMapper;

  public List<PersonaDto> listaTodos() {
    log.info("Obteniendo lista de personas");

    List<Persona> personas = personaRepository.findAll();

    return personas.stream().map(personaMapper::toDto).collect(Collectors.toList());
  }

  public PersonaDto obtenerPorId(Long id) {
    log.info("Obteniendo persona con ID: {}", id);

    Persona persona = personaRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + id));

    return personaMapper.toDto(persona);
  }

  public PersonaDto crear(CrearPersonaRequest request) {
    Persona persona = personaMapper.fromRequest(request).build();

    Persona saved = personaRepository.save(persona);
    log.info("Persona creada con ID: {}", saved.getId());

    return personaMapper.toDto(saved);
  }

  public void eliminarPorId(Long id) {
    log.info("Eliminando persona con ID: {}", id);

    personaRepository.deleteById(id);

    log.info("Persona eliminada con ID: {}", id);
  }

  public Persona obtenerEntidadPorId(Long id) {
    log.info("Obteniendo persona con ID: {}", id);

    return personaRepository.findById(id).orElse(null);
  }

  public PersonaDto actualizar(Long id, CrearPersonaRequest request) {
    log.info("Actualizando persona con ID: {}", id);

    Persona personaExistente = personaRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + id));

    Persona.PersonaBuilder personaBuilder = personaMapper.fromRequest(request);
    Persona personaActualizada = personaBuilder.id(personaExistente.getId()).build();

    personaActualizada = personaRepository.save(personaActualizada);

    log.info("Persona actualizada con ID: {}", personaActualizada.getId());
    return personaMapper.toDto(personaActualizada);
  }

}