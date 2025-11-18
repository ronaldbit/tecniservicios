package com.example.simplemvc.dto;

import com.example.simplemvc.model.enums.EstadoEntidad;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto {
    private Long idProducto;
    private String codigo;
    private String nombre;
    private MarcaDto marca;
    private CategoriaDto categoria;
    private BigDecimal precio;
    private String unidad;
    private Long idImpuesto;
    private BigDecimal stockMinimo;
    private BigDecimal stockActual;
    private BigDecimal precioOnline;
    private String descripcion;
    private Boolean destacado;
    private EstadoEntidad estado;
    private String imagenes;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Método auxiliar para acceder a las imágenes como lista
    public List<String> getImagenesList() {
        if (imagenes == null || imagenes.isEmpty()) return List.of();
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(imagenes, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return List.of();
        }
    }
}
