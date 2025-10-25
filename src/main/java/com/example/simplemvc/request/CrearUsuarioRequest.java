package com.example.simplemvc.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearUsuarioRequest {
  private Long personaId;
  private String correo;
  private String password;
}
