package com.example.simplemvc.model;

import com.example.simplemvc.dto.CajaMovimientoDto;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

@Mapper(
    componentModel = "spring",
    uses = {StringUtilsMapper.class, CajaMapper.class, UsuarioMapper.class})
public interface CajaMovimientoMapper extends BasicMapper<CajaMovimiento, CajaMovimientoDto> {
  @ObjectFactory
  default CajaMovimiento.CajaMovimientoBuilder createBuilder(CajaMovimientoDto dto) {
    return CajaMovimiento.builder();
  }

  @Mapping(target = "caja", source = "caja")
  @Mapping(target = "usuario", source = "usuario")
  CajaMovimiento toDomain(CajaMovimientoDto dto);
}
