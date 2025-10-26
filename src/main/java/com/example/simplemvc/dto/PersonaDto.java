package com.example.simplemvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PersonaDto {
  private Long id;
  private String dni;
  private String nombre;
  private String apellido;
  private String direccion;
}
