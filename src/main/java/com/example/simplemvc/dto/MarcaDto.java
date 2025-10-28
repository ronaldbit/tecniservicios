package com.example.simplemvc.dto;

import com.example.simplemvc.model.enums.EstadoEntidad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarcaDto {
  private Long id;
  private String nombre;
  private EstadoEntidad estado;
}
