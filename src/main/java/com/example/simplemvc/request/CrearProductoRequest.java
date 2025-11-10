package com.example.simplemvc.request;

import com.example.simplemvc.model.enums.EstadoEntidad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearProductoRequest {
    private String codigo;
    private String nombre;
    private Long idMarca;
    private Long idCategoria;
    private BigDecimal precio;
    private String unidad;
    private Long idImpuesto;
    private BigDecimal stockMinimo;
    private BigDecimal stockActual;
    private EstadoEntidad estado;
    private String imagenes;
}
