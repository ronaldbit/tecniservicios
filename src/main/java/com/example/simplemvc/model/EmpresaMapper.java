package com.example.simplemvc.model;

import com.example.simplemvc.dto.EmpresaDto;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

@Mapper(
    componentModel = "spring",
    uses = {StringUtilsMapper.class})
public interface EmpresaMapper extends BasicMapper<Empresa, EmpresaDto> {

  @ObjectFactory
  default Empresa.EmpresaBuilder createBuilder(EmpresaDto dto) {
    return Empresa.builder();
  }
}
