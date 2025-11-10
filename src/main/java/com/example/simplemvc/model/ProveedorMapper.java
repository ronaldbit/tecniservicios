package com.example.simplemvc.model;

import com.example.simplemvc.dto.ProveedorDto;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;
import com.example.simplemvc.request.CrearProveedorRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = StringUtilsMapper.class)
public interface ProveedorMapper extends BasicMapper<Proveedor, ProveedorDto> {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "estado", ignore = true)
  Proveedor toEntity(CrearProveedorRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "estado", ignore = true)
  void updateEntityFromRequest(@MappingTarget Proveedor proveedor, CrearProveedorRequest request);
}
