package com.example.simplemvc.request;

import lombok.Data;

@Data
public class LoginUsuarioRequest {
  private String correo;
  private String password;
}
