package com.example.simplemvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AfectacionDto {
  private Long id;
  private String codigo;
  private String descripcion;
  private Integer gravaIgv;
  private Boolean esGratuita;
  private Boolean esExportacion;
}
