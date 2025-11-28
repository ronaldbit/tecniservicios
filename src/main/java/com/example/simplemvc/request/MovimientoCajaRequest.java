package com.example.simplemvc.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MovimientoCajaRequest {
  private String tipo;
  private BigDecimal monto;
  private String motivo;
}
