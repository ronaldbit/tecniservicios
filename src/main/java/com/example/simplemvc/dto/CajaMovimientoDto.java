package com.example.simplemvc.dto;

import com.example.simplemvc.model.enums.OrigenMovimiento;
import com.example.simplemvc.model.enums.TipoMovimiento;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CajaMovimientoDto {
  private Long id;
  private CajaDto caja;
  private LocalDateTime fecha;
  private TipoMovimiento tipo;
  private OrigenMovimiento origen;
  private String observacion;
  private UsuarioDto usuario;
}
