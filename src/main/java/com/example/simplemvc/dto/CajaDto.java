package com.example.simplemvc.dto;

import com.example.simplemvc.model.enums.EstadoCaja;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CajaDto {
  private Long id;
  private SucursalDto sucursal;
  private String codigo;
  private EstadoCaja estado;
  private Double saldoInicial;
  private UsuarioDto abiertoPor;
  private LocalDateTime abiertoEn;
  private UsuarioDto cerradoPor;
  private LocalDateTime cerradoEn;
  private Double saldoCierre;
  private String observacion;
}
