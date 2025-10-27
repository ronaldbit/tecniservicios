package com.example.simplemvc.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.simplemvc.dto.PersonaDto;
import com.example.simplemvc.request.CrearPersonaRequest;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;

@Mapper(componentModel = "spring", uses = {
    StringUtilsMapper.class })
public interface PersonaMapper extends BasicMapper<Persona, PersonaDto> {
  
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  Persona.PersonaBuilder fromRequest(CrearPersonaRequest request);
}
