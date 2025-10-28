package com.example.simplemvc.model;

import com.example.simplemvc.dto.PersonaDto;
import com.example.simplemvc.request.CrearPersonaRequest;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

@Mapper(
    componentModel = "spring",
    uses = {StringUtilsMapper.class, TipoDocumentoMapper.class})
public interface PersonaMapper extends BasicMapper<Persona, PersonaDto> {

  @ObjectFactory
  default Persona.PersonaBuilder createBuilder(PersonaDto dto) {
    return Persona.builder();
  }

  @Mapping(target = "tipoDocumento", source = "tipoDocumento")
  Persona toDomain(PersonaDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "tipoDocumento", ignore = true)
  Persona.PersonaBuilder fromRequest(CrearPersonaRequest request);
}
