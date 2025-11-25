package com.example.simplemvc.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DetalleCotizacionItem {
  private Long idDetalle;
  private BigDecimal precioUnitario;
}