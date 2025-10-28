package com.example.simplemvc.request;

import lombok.Data;

@Data
public class CrearUsuarioRequest {
  private Long personaId;
  private Long sucursalId;
  private Long rolId;
  private String nombreUsuario;
  private String password;
  private long estadoEntidad;
}
