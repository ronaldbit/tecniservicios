package com.example.simplemvc.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.simplemvc.model.enums.EstadoEntidad;

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
@Table(name = "sucursal")
@SQLDelete(sql = "UPDATE sucursal SET estado = 0 WHERE id = ?")
@SQLRestriction("estado <> 0")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "empresa_id", nullable = false)
  private Empresa empresa;

  @Column(columnDefinition = "varchar(120)", nullable = false)
  private String nombre;

  @Column(columnDefinition = "varchar(300)", nullable = false)
  private String direccion;

  @Column(columnDefinition = "varchar(50)", nullable = false)
  private String telefono;

  @Builder.Default
  @Enumerated(EnumType.ORDINAL)
  @Column(nullable = false)
  private EstadoEntidad estado = EstadoEntidad.ACTIVO;

  @Builder.Default
  @Column(nullable = false)
  private LocalDateTime fechaCreacion = LocalDateTime.now();

  @Builder.Default
  @Column(nullable = false)
  private LocalDateTime fechaActualizacion = LocalDateTime.now();
}
