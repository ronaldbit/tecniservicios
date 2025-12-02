package com.example.simplemvc.dto;

import java.math.BigDecimal;

public interface TopProductoDTO {
  String getNombreProducto();

  String getCodigoProducto();

  BigDecimal getCantidadTotal();

  BigDecimal getTotalVendido();
}