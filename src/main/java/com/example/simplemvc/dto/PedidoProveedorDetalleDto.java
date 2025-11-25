package com.example.simplemvc.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProveedorDetalleDto {
  private Long id;
  private Long idProducto;
  private String nombreProducto;
  private BigDecimal cantidad;
  private BigDecimal precioUnitario;
  private BigDecimal recibidoCantidad;
  private boolean recibido;
}