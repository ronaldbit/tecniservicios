package com.example.simplemvc.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VentaDetalleDto {

    private Long idProducto;    
    private String nombreProducto; 
    private String codigoProducto; 
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuentoUnitario;
    private BigDecimal subtotal;
}