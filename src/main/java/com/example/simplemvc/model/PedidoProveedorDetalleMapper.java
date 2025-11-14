package com.example.simplemvc.model;

import com.example.simplemvc.dto.PedidoProveedorDetalleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PedidoProveedorDetalleMapper {

    @Mapping(source = "producto.idProducto", target = "idProducto")
    @Mapping(source = "producto.nombre", target = "nombreProducto")
    PedidoProveedorDetalleDto toDto(PedidoProveedorDetalle entity);
}