package com.example.simplemvc.model;


import com.example.simplemvc.dto.ProductoDto;
import com.example.simplemvc.request.CrearProductoRequest;
import com.example.simplemvc.shared.mapper.BasicMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { StringUtilsMapper.class, MarcaMapper.class, CategoriaMapper.class })
public interface ProductoMapper extends BasicMapper<Producto, ProductoDto> {
    @Mapping(source = "idMarca", target = "marca.id")
    @Mapping(source = "idCategoria", target = "categoria.idCategoria")
    @Mapping(target = "idProducto", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Producto toEntity(CrearProductoRequest requestDto);

    @Mapping(source = "idMarca", target = "marca.id")
    @Mapping(source = "idCategoria", target = "categoria.idCategoria")
    @Mapping(target = "idProducto", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(@MappingTarget Producto producto, CrearProductoRequest requestDto);
}
