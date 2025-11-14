package com.example.simplemvc.model;

import com.example.simplemvc.dto.AfectacionDto;
import com.example.simplemvc.model.Afectacion.AfectacionBuilder;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

@Mapper(
    componentModel = "spring",
    uses = {StringUtilsMapper.class})
public interface AfectacionMapper extends BasicMapper<Afectacion, AfectacionDto> {
  @ObjectFactory
  default Afectacion.AfectacionBuilder createBuilder(AfectacionDto dto) {
    return Afectacion.builder();
  }
}
