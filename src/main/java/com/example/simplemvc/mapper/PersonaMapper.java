package com.example.simplemvc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

import com.example.simplemvc.dto.PersonaDto;
import com.example.simplemvc.model.Persona;
import com.example.simplemvc.request.CrearPersonaRequest;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;

@Mapper(componentModel = "spring", uses = { StringUtilsMapper.class })
public interface PersonaMapper extends BasicMapper<Persona, PersonaDto> {

  @ObjectFactory
  default Persona.PersonaBuilder personaBuilderFromDto(PersonaDto dto) {
    return Persona.builder();
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  Persona.PersonaBuilder fromRequest(CrearPersonaRequest request);
}
