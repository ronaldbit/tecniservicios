package com.example.simplemvc.model;

import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

import com.example.simplemvc.dto.EmpresaDto;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;

@Mapper(componentModel = "spring", uses = { StringUtilsMapper.class })
public interface EmpresaMapper extends BasicMapper<Empresa, EmpresaDto> {

  @ObjectFactory
  default Empresa.EmpresaBuilder createBuilder(EmpresaDto dto) {
    return Empresa.builder();
  }
}
