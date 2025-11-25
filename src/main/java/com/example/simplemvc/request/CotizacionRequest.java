package com.example.simplemvc.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class CotizacionRequest {
  private BigDecimal costoCotizacion;
  private LocalDate fechaEntregaEsperada;
  private List<DetalleCotizacionItem> detalles;
}
