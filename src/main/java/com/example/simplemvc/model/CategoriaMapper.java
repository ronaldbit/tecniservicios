package com.example.simplemvc.model;

import com.example.simplemvc.dto.CategoriaDto;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

@Mapper(
    componentModel = "spring",
    uses = {StringUtilsMapper.class}
)
public interface CategoriaMapper extends BasicMapper<Categoria, CategoriaDto> {

    @ObjectFactory
    default Categoria toEntity(CategoriaDto dto) {
        return Categoria.builder().build();
    }
}