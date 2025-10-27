package com.example.simplemvc.request;

import lombok.Data;

@Data
public class CrearUsuarioRequest {
  private Long personaId;
  private Long sucursalId;
  private String nombreUsuario;
  private String password;
}
