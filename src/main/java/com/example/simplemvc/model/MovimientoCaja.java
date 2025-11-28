package com.example.simplemvc.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.simplemvc.model.enums.TipoMovimiento;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "caja_movimientos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoCaja {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "sesion_id", nullable = false)
  private CajaSesion sesion;

  @ManyToOne
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @Enumerated(EnumType.STRING)
  private TipoMovimiento tipo;

  private BigDecimal monto;

  private String descripcion;

  private LocalDateTime fecha;
}