package com.example.simplemvc.dto;

import com.example.simplemvc.model.enums.EstadoEntidad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumentoDto {
  private Long id;
  private String codigo;
  private EstadoEntidad estado;
}
