package com.example.simplemvc.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "empresa", uniqueConstraints = {
    @UniqueConstraint(columnNames = "ruc", name = "uk_empresa_ruc")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, nullable = false)
  private Long id;

  @Column(columnDefinition = "varchar(20)", nullable = false)
  private String ruc;

  @Column(columnDefinition = "varchar(200)", nullable = false)
  private String razonSocial;

  @Column(columnDefinition = "varchar(200)", nullable = false)
  private String nombreComercial;

  @Column(columnDefinition = "varchar(300)", nullable = false)
  private String direccion;

  @Column(columnDefinition = "varchar(50)", nullable = false)
  private String telefono;

  @Column(columnDefinition = "varchar(120)", nullable = false)
  private String email;

  @Builder.Default
  @Column(columnDefinition = "decimal(5,2)", nullable = false)
  private Double igvPorDefecto = 18.0;

  @Builder.Default
  @Column(columnDefinition = "varchar(3)", nullable = false)
  private String monedaBase = "PEN";

  @Builder.Default
  @Column(nullable = false)
  private LocalDateTime fechaCreacion = LocalDateTime.now();

  @Builder.Default
  @Column(nullable = false)
  private LocalDateTime fechaActualizacion = LocalDateTime.now();
}
