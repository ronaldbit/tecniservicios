package com.example.simplemvc.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.simplemvc.dto.PersonaDto;
import com.example.simplemvc.mapper.PersonaMapper;
import com.example.simplemvc.model.Persona;
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

  public PersonaDto crear(CrearPersonaRequest request) {
    log.info("Creando persona");

    Persona persona = personaMapper.fromRequest(request).build();

    persona = personaRepository.save(persona);

    log.info("Persona creada con ID: {}", persona.getId());
    return personaMapper.toDto(persona);
  }

  public void eliminarPorId(UUID id) {
    log.info("Eliminando persona con ID: {}", id);

    personaRepository.deleteById(id);

    log.info("Persona eliminada con ID: {}", id);
  }

  public Persona obtenerEntidadPorId(UUID id) {
    log.info("Obteniendo persona con ID: {}", id);

    return personaRepository.findById(id).orElse(null);
  }
}