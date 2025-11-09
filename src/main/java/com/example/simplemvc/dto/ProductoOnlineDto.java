package com.example.simplemvc.dto;

import lombok.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoOnlineDto {
    private Long idProductoOnline;
    private ProductoDto producto;
    private BigDecimal precioOnline;
    private String descripcion;
    private String imagenes;
    private boolean destacado;
    private boolean publicado;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
