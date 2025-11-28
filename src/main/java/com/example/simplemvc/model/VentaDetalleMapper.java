package com.example.simplemvc.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.simplemvc.dto.VentaDetalleDto;

@Mapper(componentModel = "spring")
public interface VentaDetalleMapper {
  @Mapping(source = "producto.idProducto", target = "idProducto")
  @Mapping(source = "producto.nombre", target = "nombreProducto")
  @Mapping(source = "producto.codigo", target = "codigoProducto")
  VentaDetalleDto toDto(VentaDetalle entity);
}