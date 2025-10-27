package com.example.simplemvc.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDto {
  private Long id;
  private String ruc;
  private String razonSocial;
  private String nombreComercial;
  private String direccion;
  private String telefono;
  private String email;
  private Double igvPorDefecto;
  private String monedaBase;
  private LocalDateTime fechaCreacion;
  private LocalDateTime fechaActualizacion;
}
