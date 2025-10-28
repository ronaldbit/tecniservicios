package com.example.simplemvc.dto;

import com.example.simplemvc.model.enums.EstadoEntidad;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {
  private Long id;
  private PersonaDto persona;
  private SucursalDto sucursal;
  private String nombreUsuario;
  private List<RolDto> roles;
  private EstadoEntidad estado;
  private LocalDateTime fechaCreacion;
  private LocalDateTime fechaActualizacion;
}
