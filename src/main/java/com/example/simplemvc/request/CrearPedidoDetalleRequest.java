package com.example.simplemvc.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearPedidoDetalleRequest {
    private Long idProducto;
    private BigDecimal cantidad;
}