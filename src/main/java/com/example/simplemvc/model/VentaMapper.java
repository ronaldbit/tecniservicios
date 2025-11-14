package com.example.simplemvc.model;

import com.example.simplemvc.dto.VentaDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {VentaDetalleMapper.class})
public interface VentaMapper {
    @Mapping(source = "vendedor.id", target = "idUsuarioVendedor")
    @Mapping(source = "vendedor.nombreUsuario", target = "nombreVendedor")
    VentaDto toDto(Venta entity);
}