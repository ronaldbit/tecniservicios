package com.example.simplemvc.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;

import com.example.simplemvc.dto.PermisoDto;
import com.example.simplemvc.request.CrearPermisoRequest;
import com.example.simplemvc.shared.config.LombokBuilderConfig;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;


@Mapper(componentModel = "spring", uses = { StringUtilsMapper.class }, unmappedSourcePolicy = ReportingPolicy.IGNORE, config = LombokBuilderConfig.class)
public interface PermisoMapper extends BasicMapper<Permiso, PermisoDto> {
    
    @ObjectFactory
    default Permiso.PermisoBuilder createBuilder(PermisoDto dto) {
        return Permiso.builder();
    }

    @Mapping(target = "rol", ignore = true)
    Permiso toDomain(PermisoDto dto);
    PermisoDto toDto(Permiso entity);

    Permiso.PermisoBuilder fromRequest(CrearPermisoRequest request);


}