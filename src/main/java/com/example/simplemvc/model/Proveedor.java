package com.example.simplemvc.model;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "proveedor")
@SQLDelete(sql = "UPDATE proveedor SET estado = 0 WHERE id = ?")
@SQLRestriction("estado <> 0")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, nullable = false)
  private Long id;

  @Column(columnDefinition = "varchar(20)", nullable = false)
  private String ruc;

  @Column(columnDefinition = "varchar(200)", nullable = false)
  private String razonSocial;

  @Column(columnDefinition = "varchar(300)", nullable = true)
  private String direccion;

  @Column(columnDefinition = "varchar(50)", nullable = true)
  private String telefono;

  @Column(columnDefinition = "varchar(120)", nullable = true)
  private String email;

  @Builder.Default
  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private EstadoEntidad estado = EstadoEntidad.ACTIVO;
}
