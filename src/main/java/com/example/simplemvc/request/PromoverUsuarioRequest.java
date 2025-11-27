package com.example.simplemvc.request;

import lombok.Data;

@Data
public class PromoverUsuarioRequest {
  private String dni;
  private Long nuevoRolId;
}