package com.example.simplemvc.model;

import com.example.simplemvc.model.enums.EstadoEntidad;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "almacen")
@SQLDelete(sql = "UPDATE almacen SET estado = 0 WHERE id = ?")
@SQLRestriction("estado <> 0")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Almacen {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "sucursal_id", nullable = false)
  private Sucursal sucursal;

  @Column(columnDefinition = "varchar(120)", nullable = false)
  private String nombre;

  @Builder.Default
  @Column(nullable = false)
  private EstadoEntidad estado = EstadoEntidad.ACTIVO;
}
