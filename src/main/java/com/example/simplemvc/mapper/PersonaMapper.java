package com.example.simplemvc.mapper;

import org.mapstruct.Mapper;

import com.example.simplemvc.dto.PersonaDto;
import com.example.simplemvc.model.Persona;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;

@Mapper(componentModel = "spring", uses = { StringUtilsMapper.class })
public interface PersonaMapper extends BasicMapper<Persona, PersonaDto> {

}
