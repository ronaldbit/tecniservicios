package com.example.simplemvc.request;

import com.example.simplemvc.model.enums.EstadoEntidad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    private BigDecimal precioOnline;
    private String descripcion;
    private Boolean destacado;
    private EstadoEntidad estado;
    private List<MultipartFile> imagenes;
}
