package com.example.simplemvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaDto {
  private Long id;
  private TipoDocumentoDto tipoDocumento;
  private String numeroDocumento;
  private String tipoPersona;
  private String nombres;
  private String apellidos;
  private String razonSocial;
  private String email;
  private String telefono;
  private String direccion;
}
