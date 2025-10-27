package com.example.simplemvc.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;

import com.example.simplemvc.dto.UsuarioRolDto;
import com.example.simplemvc.request.CrearUsuarioRol;
import com.example.simplemvc.shared.config.LombokBuilderConfig;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;

@Mapper(componentModel = "spring", uses = { StringUtilsMapper.class,
    PermisoMapper.class }, unmappedSourcePolicy = ReportingPolicy.IGNORE, config = LombokBuilderConfig.class)
public interface UsuarioRolMapper extends BasicMapper<UsuarioRol, UsuarioRolDto> {
  
  @ObjectFactory
  default UsuarioRol.UsuarioRolBuilder createBuilder(UsuarioRolDto dto) {
    return UsuarioRol.builder();
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "permisos", ignore = true)
  UsuarioRol.UsuarioRolBuilder fromRequest(CrearUsuarioRol request);
}