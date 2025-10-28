package com.example.simplemvc.request;

import lombok.Data;

@Data
public class CrearPermisoRequest {
  private Long rolId;
  private String nombre;
  private String path;
}
