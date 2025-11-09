package com.example.simplemvc.request;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearProductoOnlineRequest {
    private Long idProducto;
    private BigDecimal precioOnline;
    private String descripcion;
    private String imagenes; 
    private boolean destacado;
    private boolean publicado;
}
