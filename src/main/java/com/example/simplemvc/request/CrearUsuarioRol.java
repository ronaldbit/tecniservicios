package com.example.simplemvc.request;

import java.util.List;
import lombok.Data;

@Data
public class CrearUsuarioRol {
  private String nombre;
  private String descripcion;
  private List<CrearPermisoRequest> permisos;
}
