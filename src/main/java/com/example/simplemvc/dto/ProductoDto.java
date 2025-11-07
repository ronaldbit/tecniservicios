package com.example.simplemvc.dto;

import com.example.simplemvc.model.enums.EstadoEntidad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
    private EstadoEntidad estado;    
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
