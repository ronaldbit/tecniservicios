package com.example.simplemvc.dto;

import java.util.List;
import java.util.UUID;

import com.example.simplemvc.model.UsuarioRol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {
  private UUID id;
  private PersonaDto persona;
  private String correo;
  private List<UsuarioRol> roles;
}
