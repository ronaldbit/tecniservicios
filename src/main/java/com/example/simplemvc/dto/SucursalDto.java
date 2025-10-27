package com.example.simplemvc.dto;

import java.time.LocalDateTime;

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
public class SucursalDto {
  private Long id;
  private EmpresaDto empresa;
  private String nombre;
  private String direccion;
  private String telefono;
  private EstadoEntidad estado;
  private LocalDateTime fechaCreacion;
  private LocalDateTime fechaActualizacion;
}
