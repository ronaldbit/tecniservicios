package com.example.simplemvc.model;

import com.example.simplemvc.dto.PedidoProveedorDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PedidoProveedorDetalleMapper.class})
public interface PedidoProveedorMapper {
    @Mapping(source = "proveedor.id", target = "idProveedor")
    @Mapping(source = "proveedor.razonSocial", target = "razonSocialProveedor")   
    PedidoProveedorDto toDto(PedidoProveedor entity);
}