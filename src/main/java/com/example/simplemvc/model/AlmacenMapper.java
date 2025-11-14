package com.example.simplemvc.model;

import com.example.simplemvc.dto.AlmacenDto;
import com.example.simplemvc.model.Almacen.AlmacenBuilder;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

@Mapper(
    componentModel = "spring",
    uses = {StringUtilsMapper.class, SucursalMapper.class})
public interface AlmacenMapper extends BasicMapper<Almacen, AlmacenDto> {
  @ObjectFactory
  default Almacen.AlmacenBuilder createBuilder(AlmacenDto dto) {
    return Almacen.builder();
  }

  @Mapping(target = "sucursal", source = "sucursal")
  Almacen toDomain(AlmacenDto dto);
}
