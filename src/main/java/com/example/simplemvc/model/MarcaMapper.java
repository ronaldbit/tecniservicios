package com.example.simplemvc.model; 

import com.example.simplemvc.dto.MarcaDto;
import com.example.simplemvc.request.CrearMarcaRequest;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

@Mapper(componentModel = "spring", uses = { StringUtilsMapper.class })
public interface MarcaMapper extends BasicMapper<Marca, MarcaDto> {
  @ObjectFactory
  default Marca toEntity(CrearMarcaRequest request) {   
    return Marca.builder()
        .nombre(request.getNombre())
        .descripcion(request.getDescripcion())
        .build();
  }
  
}