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
public class ProveedorDto {
  private Long id;
  private String ruc;
  private String razonSocial;
  private String direccion;
  private String telefono;
  private String email;
  private EstadoEntidad estado;
}
