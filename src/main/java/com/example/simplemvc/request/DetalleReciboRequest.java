package com.example.simplemvc.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DetalleReciboRequest {
  private Long idDetalle;
  private BigDecimal recibidoCantidad;
}
