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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(
    name = "tipo_documento",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = "codigo", name = "uk_tipo_documento_codigo")
    })
@SQLDelete(sql = "UPDATE tipo_documento SET estado = 0 WHERE id = ?")
@SQLRestriction("estado <> 0")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumento {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "varchar(5)", nullable = false)
  private String codigo;

  @Builder.Default
  @Enumerated(EnumType.ORDINAL)
  private EstadoEntidad estado = EstadoEntidad.ACTIVO;
}
