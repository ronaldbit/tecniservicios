package com.example.simplemvc.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AbrirCajaRequest {
  private Long cajaId;
  private BigDecimal montoInicial;
  private String notas;
}