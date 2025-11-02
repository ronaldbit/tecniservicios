package com.example.simplemvc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "persona",
    uniqueConstraints = {
      @UniqueConstraint(
          columnNames = {"tipoDocumento", "numeroDocumento"},
          name = "uk_persona_tipo_numero_documento")
    })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Persona {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "tipo_documento_id", nullable = false)
  private TipoDocumento tipoDocumento;

  @Column(columnDefinition = "varchar(20)", nullable = false)
  private String numeroDocumento;

  @Column(columnDefinition = "varchar(10)", nullable = false)
  private String tipoPersona;

  @Column(columnDefinition = "varchar(120)", nullable = false)
  private String nombres;

  @Column(columnDefinition = "varchar(120)", nullable = false)
  private String apellidos;

  @Column(columnDefinition = "varchar(200)", nullable = false)
  private String razonSocial;

  @Column(columnDefinition = "varchar(120)", nullable = false)
  private String email;

  @Column(columnDefinition = "varchar(50)", nullable = false)
  private String telefono;

  @Column(columnDefinition = "varchar(300)", nullable = false)
  private String direccion;

  @Column(columnDefinition = "boolean", nullable = false)
  private Boolean estado;

  @Column(columnDefinition = "varchar(255)", nullable = true)
  private String tokenVerificacionEmail;

  @Column(columnDefinition = "boolean", nullable = false)
  private Boolean emailVerificado;
}
