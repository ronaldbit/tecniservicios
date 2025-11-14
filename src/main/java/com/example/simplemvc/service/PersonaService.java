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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonaService {
  private final PersonaRepository personaRepository;
  private final PersonaMapper personaMapper;
  private final TipoDocumentoService tipoDocumentoService;

  @Autowired
  private ServicioCorreo servicioCorreo;

  public List<PersonaDto> listaTodos() {
    log.info("Obteniendo lista de personas");
    List<Persona> personas = personaRepository.findAll();
    return personas.stream().map(personaMapper::toDto).collect(Collectors.toList());
  }

  public PersonaDto obtenerPorId(Long id) {
    log.info("Obteniendo persona con ID: {}", id);
    Persona persona = personaRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + id));
    return personaMapper.toDto(persona);
  }

  public PersonaDto crear(CrearPersonaRequest request) {
    log.info("Creando nueva persona");

    TipoDocumento tipoDocumento = tipoDocumentoService
        .obtenerEntidadPorId(request.getTipoDocumentoId())
        .orElseThrow(() -> new IllegalArgumentException(
            "Tipo de documento no encontrado con ID: " + request.getTipoDocumentoId()));

    Optional<Persona> personaOpt = personaRepository.findByNumeroDocumento(request.getNumeroDocumento());
    Optional<Persona> personaEmailOpt = personaRepository.findByEmail(request.getEmail());

    if (personaEmailOpt.isPresent()) {
      Persona personaEmail = personaEmailOpt.get();
      if (Boolean.TRUE.equals(personaEmail.getEstado())
          && (!personaOpt.isPresent() || !personaEmail.getId().equals(personaOpt.get().getId()))) {
        throw new IllegalArgumentException("Ya existe una persona activa con el email: " + request.getEmail());
      }
    }
    if (personaOpt.isPresent()) {
      Persona existente = personaOpt.get();

      if (Boolean.TRUE.equals(existente.getEstado())) {
        throw new IllegalArgumentException(
            "Ya existe una persona activa con el número de documento: " + request.getNumeroDocumento());
      }
      log.info("Reactivando persona existente con ID: {}", existente.getId());
      existente.setTipoDocumento(tipoDocumento);
      existente.setTipoPersona(request.getTipoPersona());
      existente.setNombres(request.getNombres());
      existente.setApellidos(request.getApellidos());
      existente.setRazonSocial(request.getRazonSocial());
      existente.setEmail(request.getEmail());
      existente.setTelefono(request.getTelefono());
      existente.setDireccion(request.getDireccion());
      existente.setEstado(true);
      existente.setEmailVerificado(false);
      existente.setTokenVerificacionEmail(JwtTokenEmail.generateToken(request.getEmail()));
      Persona reactivada = personaRepository.save(existente);
      servicioCorreo.enviarVerificacionCorreo(existente.getEmail(), existente.getTokenVerificacionEmail());
      log.info("Persona reactivada con ID: {}", reactivada.getId());
      return personaMapper.toDto(reactivada);
    }
    Persona persona = personaMapper.fromRequest(request)
        .tipoDocumento(tipoDocumento)
        .estado(true)
        .emailVerificado(false)
        .tokenVerificacionEmail(JwtTokenEmail.generateToken(request.getEmail()))
        .build();

    Persona saved = personaRepository.save(persona);
    servicioCorreo.enviarVerificacionCorreo(persona.getEmail(), persona.getTokenVerificacionEmail());
    log.info("Persona creada con ID: {}", saved.getId());
    return personaMapper.toDto(saved);
  }

  public void eliminarPorId(Long id) {
    log.info("Eliminando persona con ID: {}", id);
    Persona persona = personaRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + id));
    persona.setEstado(false);
    personaRepository.save(persona);
    log.info("Persona eliminada con ID: {}", id);
  }

  public Optional<Persona> obtenerEntidadPorId(Long id) {
    log.info("Obteniendo persona con ID: {}", id);
    return personaRepository.findById(id);
  }

  public PersonaDto actualizar(Long id, CrearPersonaRequest request) {
    log.info("Actualizando persona con ID: {}", id);
    Persona personaExistente = personaRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + id));

    Persona.PersonaBuilder personaBuilder = personaMapper.fromRequest(request);
    Persona personaActualizada;

    personaActualizada = personaBuilder
        .id(personaExistente.getId())
        .tipoDocumento(personaExistente.getTipoDocumento())
        .build();

    if (personaExistente.getEmail().equals(request.getEmail()) == false) {
      personaActualizada.setEmailVerificado(false);
      log.info("El email ha cambiado. Marcando emailVerificado como false para la persona con ID: {}", id);
    } else {
      personaActualizada.setEmailVerificado(personaExistente.getEmailVerificado());
    }
    personaActualizada.setEstado(personaExistente.getEstado());
    personaActualizada = personaRepository.save(personaActualizada);
    log.info("Persona actualizada con ID: {}", personaActualizada.getId());
    return personaMapper.toDto(personaActualizada);
  }
}
