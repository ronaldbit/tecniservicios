package com.example.simplemvc.model;

import com.example.simplemvc.dto.SucursalDto;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

@Mapper(
    componentModel = "spring",
    uses = {StringUtilsMapper.class, EmpresaMapper.class})
public interface SucursalMapper extends BasicMapper<Sucursal, SucursalDto> {

  @ObjectFactory
  default Sucursal.SucursalBuilder createBuilder(SucursalDto dto) {
    return Sucursal.builder();
  }

  @Mapping(target = "empresa", source = "empresa")
  Sucursal toDomain(SucursalDto dto);
}
