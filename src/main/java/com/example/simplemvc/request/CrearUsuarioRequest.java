package com.example.simplemvc.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearUsuarioRequest {
  private UUID personaId;
  private String correo;
  private String password;
}
