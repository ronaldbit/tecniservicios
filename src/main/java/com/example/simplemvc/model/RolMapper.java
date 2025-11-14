package com.example.simplemvc.model;

import com.example.simplemvc.dto.RolDto;
import com.example.simplemvc.model.Rol.RolBuilder;
import com.example.simplemvc.request.CrearUsuarioRol;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

@Mapper(
    componentModel = "spring",
    uses = {StringUtilsMapper.class, PermisoMapper.class})
public interface RolMapper extends BasicMapper<Rol, RolDto> {
  @ObjectFactory
  default Rol.RolBuilder createBuilder(RolDto dto) {
    return Rol.builder();
  }

  @Mapping(target = "permisos", source = "permisos")
  Rol toDomain(RolDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "permisos", ignore = true)
  Rol.RolBuilder fromRequest(CrearUsuarioRol request);
}
