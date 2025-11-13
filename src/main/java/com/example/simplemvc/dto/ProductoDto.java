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
    private BigDecimal stockActual;
    private BigDecimal precioOnline;
    private String descripcion;
    private Boolean destacado;    
    private EstadoEntidad estado;
    private String imagenes;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
