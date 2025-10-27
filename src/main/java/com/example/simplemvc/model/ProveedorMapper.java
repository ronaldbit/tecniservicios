package com.example.simplemvc.model;

import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

import com.example.simplemvc.dto.ProveedorDto;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;

@Mapper(componentModel = "spring", uses = { StringUtilsMapper.class })
public interface ProveedorMapper extends BasicMapper<Proveedor, ProveedorDto> {

  @ObjectFactory
  default Proveedor.ProveedorBuilder createBuilder(ProveedorDto dto) {
    return Proveedor.builder();
  }

}
