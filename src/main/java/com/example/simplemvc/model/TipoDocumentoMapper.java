package com.example.simplemvc.model;

import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

import com.example.simplemvc.dto.TipoDocumentoDto;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;

@Mapper(componentModel = "spring", uses = { StringUtilsMapper.class })
public interface TipoDocumentoMapper extends BasicMapper<TipoDocumento, TipoDocumentoDto> {

  @ObjectFactory
  default TipoDocumento.TipoDocumentoBuilder createBuilder(TipoDocumentoDto dto) {
    return TipoDocumento.builder();
  }
}
