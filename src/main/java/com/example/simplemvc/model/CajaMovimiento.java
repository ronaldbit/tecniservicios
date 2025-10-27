package com.example.simplemvc.model;

import java.time.LocalDateTime;

import com.example.simplemvc.model.enums.OrigenMovimiento;
import com.example.simplemvc.model.enums.TipoMovimiento;

import jakarta.persistence.Column;
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
@Table(name = "caja_movimiento")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CajaMovimiento {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, nullable = false)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "caja_id", nullable = false)
  private Caja caja;

  @Column(nullable = false)
  private LocalDateTime fecha;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private TipoMovimiento tipo;

  @Builder.Default
  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private OrigenMovimiento origen = OrigenMovimiento.OTRO;

  // TODO: private Long referenciaId;

  @Column(columnDefinition = "varchar(300)",nullable = false)
  private String observacion;

  @ManyToOne(optional = false)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;
}
