package com.example.simplemvc.dto;

import java.math.BigDecimal;

public interface TopVendedorDTO {
  String getNombreVendedor();

  Long getCantidadVentas();

  BigDecimal getTotalVendido();
}