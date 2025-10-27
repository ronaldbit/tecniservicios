package com.example.simplemvc.model;

import java.time.LocalDateTime;

import com.example.simplemvc.model.enums.EstadoCaja;

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
@Table(name = "caja")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Caja {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, nullable = false)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "sucursal_id", nullable = false)
  private Sucursal sucursal;

  @Column(columnDefinition = "varchar(30)", nullable = false)
  private String codigo;

  @Builder.Default
  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private EstadoCaja estado = EstadoCaja.CERRADA;

  @Builder.Default
  @Column(name = "saldo_inicial", nullable = false)
  private Double saldoInicial = 0.0;

  @ManyToOne(optional = true)
  @JoinColumn(name = "abierto_por_id", nullable = true)
  private Usuario abiertoPor;

  @Column(nullable = true)
  private LocalDateTime abiertoEn;

  @ManyToOne(optional = true)
  @JoinColumn(name = "cerrado_por_id", nullable = true)
  private Usuario cerradoPor;

  @Column(nullable = true)
  private LocalDateTime cerradoEn;

  @Column(name = "saldo_cierre", nullable = true)
  private Double saldoCierre;

  @Column(columnDefinition = "varchar(300)", nullable = true)
  private String observacion;
}
