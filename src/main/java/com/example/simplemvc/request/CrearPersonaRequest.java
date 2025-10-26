package com.example.simplemvc.request;

import lombok.Data;

@Data
public class CrearPersonaRequest {
  private String dni;
  private String nombre;
  private String apellido;
  private String direccion;
}
