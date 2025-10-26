package com.example.simplemvc.request;

import lombok.Data;

@Data
public class CrearUsuarioRequest {
  private Long personaId;
  private String correo;
  private String password;
}
