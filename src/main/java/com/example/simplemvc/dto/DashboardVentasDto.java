package com.example.simplemvc.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.Data;

@Data
public class DashboardVentasDto {

  private BigDecimal ingresosHoy;
  private BigDecimal ingresosAyer; 
  private Long visitasTienda; 
  private Long ordenesPendientes;
  private Long devolucionesMes;
  private BigDecimal ingresosTiendaFisica;
  private BigDecimal ingresosTiendaOnline;
  private BigDecimal ingresosCreditos;
  private BigDecimal ingresosOtros;

 
  public String getCrecimientoIngresos() {
    if (ingresosAyer == null || ingresosAyer.compareTo(BigDecimal.ZERO) == 0)
      return "+100%";
    BigDecimal diff = ingresosHoy.subtract(ingresosAyer);
    return diff.divide(ingresosAyer, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)) + "%";
  }
}