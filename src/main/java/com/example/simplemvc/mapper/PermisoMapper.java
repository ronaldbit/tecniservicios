package com.example.simplemvc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

import com.example.simplemvc.dto.PermisoDto;
import com.example.simplemvc.model.Permiso;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;

@Mapper(componentModel = "spring", uses = { StringUtilsMapper.class, UsuarioMapper.class,
    UsuarioRolMapper.class })
public interface PermisoMapper extends BasicMapper<Permiso, PermisoDto> {

  @ObjectFactory
  default Permiso.PermisoBuilder permisoBuilderFromDto(PermisoDto dto) {
    return Permiso.builder();
  }

  @Mapping(target = "rol", ignore = true)
  Permiso toDomain(PermisoDto dto);
}
