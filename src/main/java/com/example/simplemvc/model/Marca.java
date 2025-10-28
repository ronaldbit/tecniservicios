package com.example.simplemvc.model;

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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "marca")
@SQLDelete(sql = "UPDATE marca SET estado = 0 WHERE id = ?")
@SQLRestriction("estado <> 0")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Marca {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, nullable = false)
  private Long id;

  @Column(columnDefinition = "varchar(120)", nullable = false)
  private String nombre;

  @Builder.Default
  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private EstadoEntidad estado = EstadoEntidad.ACTIVO;
}
