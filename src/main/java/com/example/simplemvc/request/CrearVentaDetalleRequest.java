package com.example.simplemvc.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CrearVentaDetalleRequest {    
    private Long idProducto;    
    private BigDecimal cantidad;    
    private BigDecimal precioUnitario;     
    private BigDecimal descuentoUnitario;
}