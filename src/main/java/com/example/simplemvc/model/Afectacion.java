package com.example.simplemvc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "afectacion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Afectacion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, nullable = false)
  private Long id;

  @Column(columnDefinition = "varchar(2)", nullable = false)
  private String codigo;

  @Column(columnDefinition = "varchar(120)", nullable = false)
  private String descripcion;

  @Column(nullable = false)
  private Integer gravaIgv;

  @Builder.Default
  @Column(nullable = false)
  private Boolean esGratuita = false;

  @Builder.Default
  @Column(nullable = false)
  private Boolean esExportacion = false;
}
