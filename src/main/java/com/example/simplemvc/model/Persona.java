package com.example.simplemvc.model;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Audited
@Table(name = "persona", uniqueConstraints = {
    @UniqueConstraint(columnNames = "dni", name = "uk_persona_dni")
})
@SQLDelete(sql = "UPDATE persona SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Persona {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID id;

  @Column(columnDefinition = "varchar(8)")
  private String dni;

  @Column(columnDefinition = "varchar(50)")
  private String nombre;

  @Column(columnDefinition = "varchar(50)")
  private String apellido;

  @Column(columnDefinition = "varchar(255)", nullable = false)
  private String direccion;

  @Column(columnDefinition = "varchar(254)", nullable = false)
  private String email;

  @Builder.Default
  private boolean deleted = false;



}