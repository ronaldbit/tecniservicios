package com.example.simplemvc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

import com.example.simplemvc.dto.UsuarioRolDto;
import com.example.simplemvc.model.UsuarioRol;
import com.example.simplemvc.shared.mapper.CrudMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;

@Mapper(componentModel = "spring", uses = { StringUtilsMapper.class, UsuarioMapper.class,
    PermisoMapper.class })
public interface UsuarioRolMapper extends CrudMapper<UsuarioRol, UsuarioRolDto, UsuarioRol.UsuarioRolBuilder> {
  @ObjectFactory
  default UsuarioRol.UsuarioRolBuilder usuarioRolBuilderFromDto(UsuarioRolDto dto) {
    return UsuarioRol.builder();
  }

  @Mapping(target = "usuario", ignore = true)
  UsuarioRol toDomain(UsuarioRolDto dto);
}
