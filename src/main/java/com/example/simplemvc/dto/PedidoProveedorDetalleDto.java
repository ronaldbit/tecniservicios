package com.example.simplemvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProveedorDetalleDto {
    private Long id;     
    private Long idProducto;  
    private String nombreProducto; 
    private BigDecimal cantidad;    
    private boolean recibido;
}