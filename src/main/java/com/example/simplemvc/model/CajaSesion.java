package com.example.simplemvc.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.simplemvc.model.enums.EstadoCaja;

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
@Table(name = "caja_sesiones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CajaSesion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "caja_id", nullable = false)
  private Caja caja;

  @ManyToOne
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuarioApertura;

  private LocalDateTime fechaApertura;
  private LocalDateTime fechaCierre;

  private BigDecimal montoInicial;
  private BigDecimal montoFinal;

  @Builder.Default
  private BigDecimal totalIngresos = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal totalEgresos = BigDecimal.ZERO;

  @Enumerated(EnumType.STRING)
  private EstadoCaja estado;
}