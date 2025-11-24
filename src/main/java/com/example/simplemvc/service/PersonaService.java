package com.example.simplemvc.service;

import com.example.simplemvc.dto.PersonaDto;
import com.example.simplemvc.model.Persona;
import com.example.simplemvc.model.PersonaMapper;
import com.example.simplemvc.model.TipoDocumento;
import com.example.simplemvc.repository.PersonaRepository;
import com.example.simplemvc.request.CrearPersonaRequest;
import com.example.simplemvc.request.CrearUsuarioClienteRequest;

import jakarta.transaction.Transactional;

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

  @Transactional 
  public PersonaDto crear(CrearPersonaRequest request) {
    log.info("Procesando creación/reactivación de persona: {}", request.getNumeroDocumento());
    TipoDocumento tipoDocumento = tipoDocumentoService
        .obtenerEntidadPorId(request.getTipoDocumentoId())
        .orElseThrow(() -> new IllegalArgumentException(
            "Tipo de documento no encontrado con ID: " + request.getTipoDocumentoId()));

    Optional<Persona> personaPorDniOpt = personaRepository.findByNumeroDocumento(request.getNumeroDocumento());
    validarEmailUnico(request.getEmail(), personaPorDniOpt);
    Persona persona;

    if (personaPorDniOpt.isPresent()) {
      persona = personaPorDniOpt.get();
      if (Boolean.TRUE.equals(persona.getEstado())) {
        throw new IllegalArgumentException(
            "Ya existe una persona activa con el número de documento: " + request.getNumeroDocumento());
      }

      log.info("Reactivando persona existente con ID: {}", persona.getId());
    } else {
      log.info("Creando nueva entidad Persona");
      persona = new Persona();
    }
    actualizarDatosPersona(persona, request, tipoDocumento);
    persona.setEstado(true);
    persona.setEmailVerificado(false);
    String token = JwtTokenEmail.generateToken(request.getEmail());
    persona.setTokenVerificacionEmail(token);
    Persona saved = personaRepository.save(persona);

    try {
      servicioCorreo.enviarVerificacionCorreo(saved.getEmail(), token);
    } catch (Exception e) {
      log.error("Error al enviar correo de verificación: {}", e.getMessage());
    }

    log.info("Operación exitosa para persona ID: {}", saved.getId());
    return personaMapper.toDto(saved);
  }


  private void validarEmailUnico(String email, Optional<Persona> personaActualOpt) {
    Optional<Persona> personaPorEmailOpt = personaRepository.findByEmail(email);
    if (personaPorEmailOpt.isPresent()) {
      Persona personaConEseEmail = personaPorEmailOpt.get();
      if (Boolean.TRUE.equals(personaConEseEmail.getEstado())) {
        if (personaActualOpt.isEmpty() || !personaConEseEmail.getId().equals(personaActualOpt.get().getId())) {
          throw new IllegalArgumentException("Ya existe una persona activa con el email: " + email);
        }
      }
    }
  }

  private void actualizarDatosPersona(Persona persona, CrearPersonaRequest request, TipoDocumento tipoDocumento) {
    persona.setTipoDocumento(tipoDocumento);
    persona.setNumeroDocumento(request.getNumeroDocumento()); 
    persona.setNombres(request.getNombres());
    persona.setApellidos(request.getApellidos());
    persona.setRazonSocial(request.getRazonSocial());
    persona.setEmail(request.getEmail());
    persona.setTelefono(request.getTelefono());
    persona.setDireccion(request.getDireccion());
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

  public Persona crearDesdeClienteRequest(CrearUsuarioClienteRequest request) {
    personaRepository.findByEmail(request.getEmail()).ifPresent(existingPersona -> {

      throw new IllegalArgumentException("Ya existe una persona con el email: " + request.getEmail());
    });

    personaRepository.findByNumeroDocumento(request.getNumeroDocumento()).ifPresent(existingPersona -> {
      throw new IllegalArgumentException(
          "Ya existe una persona con el número de documento: " + request.getNumeroDocumento());
    });
    Persona per = Persona.builder()
        .tipoDocumento(
            tipoDocumentoService

                .obtenerEntidadPorId(request.getTipoDocumentoId())
                .orElseThrow(
                    () -> new IllegalArgumentException(
                        "Tipo de documento no encontrado con ID: " + request.getTipoDocumentoId())))
        .tipoPersona(request.getTipoPersona())
        .numeroDocumento(request.getNumeroDocumento())
        .razonSocial(null)
        .nombres(request.getNombres())
        .apellidos(request.getApellidos())
        .razonSocial(request.getRazonSocial())
        .email(request.getEmail())
        .telefono(request.getTelefono())
        .direccion(request.getDireccion())
        .estado(true)
        .emailVerificado(false)
        .tokenVerificacionEmail(JwtTokenEmail.generateToken(request.getEmail()))
        .build();
    personaRepository.save(per);
    return per;
  }

}
