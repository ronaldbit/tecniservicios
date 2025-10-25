package com.example.simplemvc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

import com.example.simplemvc.dto.UsuarioDto;
import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.request.CrearUsuarioRequest;
import com.example.simplemvc.shared.mapper.CrudMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;

@Mapper(componentModel = "spring", uses = { StringUtilsMapper.class,
    UsuarioRolMapper.class,
    PermisoMapper.class })
public interface UsuarioMapper extends CrudMapper<Usuario, UsuarioDto, Usuario.UsuarioBuilder> {

  @ObjectFactory
  default Usuario.UsuarioBuilder userBuilderFromDto(UsuarioDto dto) {
    return Usuario.builder();
  }

  @Mapping(target = "password", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  @Mapping(target = "persona", ignore = true)
  Usuario toDomain(UsuarioDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "persona", ignore = true)
  @Mapping(target = "rol", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  Usuario.UsuarioBuilder fromRequest(CrearUsuarioRequest request);
}
