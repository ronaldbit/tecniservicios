package com.example.simplemvc.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearPersonaRequest {
  private String dni;
  private String nombre;
  private String apellido;
  private String direccion;
}
