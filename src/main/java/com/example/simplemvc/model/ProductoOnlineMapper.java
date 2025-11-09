package com.example.simplemvc.model;

import com.example.simplemvc.dto.ProductoOnlineDto;
import com.example.simplemvc.request.CrearProductoOnlineRequest;
import com.example.simplemvc.shared.mapper.BasicMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { ProductoMapper.class })
public interface ProductoOnlineMapper extends BasicMapper<ProductoOnline, ProductoOnlineDto> {

    @Mapping(source = "idProducto", target = "producto.idProducto")
    @Mapping(target = "idProductoOnline", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProductoOnline toEntity(CrearProductoOnlineRequest request);

    @Mapping(source = "idProducto", target = "producto.idProducto")
    @Mapping(target = "idProductoOnline", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(@MappingTarget ProductoOnline productoOnline, CrearProductoOnlineRequest request);
}
