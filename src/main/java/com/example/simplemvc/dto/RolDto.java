package com.example.simplemvc.dto;

import java.util.List;

import com.example.simplemvc.model.Permiso;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolDto {
  private Long id;
  private String nombre;
  private String descripcion;
  private List<Permiso> permisos;
}
