package com.example.simplemvc.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

import com.example.simplemvc.dto.CajaDto;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;

@Mapper(componentModel = "spring", uses = { StringUtilsMapper.class, SucursalMapper.class, UsuarioMapper.class })
public interface CajaMapper extends BasicMapper<Caja, CajaDto> {
  @ObjectFactory
  default Caja.CajaBuilder createBuilder(CajaDto dto) {
    return Caja.builder();
  }

  @Mapping(target = "sucursal", source = "sucursal")
  @Mapping(target = "abiertoPor", source = "abiertoPor")
  @Mapping(target = "cerradoPor", source = "cerradoPor")
  Caja toDomain(CajaDto dto);
}
