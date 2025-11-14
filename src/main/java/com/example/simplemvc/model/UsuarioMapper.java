package com.example.simplemvc.model;

import com.example.simplemvc.dto.UsuarioDto;
import com.example.simplemvc.model.Usuario.UsuarioBuilder;
import com.example.simplemvc.request.CrearUsuarioRequest;
import com.example.simplemvc.shared.mapper.CrudMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

@Mapper(
    componentModel = "spring",
    uses = {
      StringUtilsMapper.class,
      PersonaMapper.class,
      SucursalMapper.class,
      RolMapper.class,
      PermisoMapper.class
    })
public interface UsuarioMapper extends CrudMapper<Usuario, UsuarioDto, Usuario.UsuarioBuilder> {

  @ObjectFactory
  default Usuario.UsuarioBuilder createBuilder(UsuarioDto dto) {
    return Usuario.builder();
  }

  @Mapping(target = "password", ignore = true)
  @Mapping(target = "persona", source = "persona")
  @Mapping(target = "sucursal", source = "sucursal")
  @Mapping(target = "roles", source = "roles")
  Usuario toDomain(UsuarioDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "persona", ignore = true)
  @Mapping(target = "sucursal", ignore = true)
  @Mapping(target = "roles", ignore = true)
  @Mapping(target = "estado", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  @Mapping(target = "fechaActualizacion", ignore = true)
  Usuario.UsuarioBuilder fromRequest(CrearUsuarioRequest request);
}
