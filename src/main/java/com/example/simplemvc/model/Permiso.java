package com.example.simplemvc.model;

import java.util.UUID;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "permisos", uniqueConstraints = {
    @UniqueConstraint(columnNames = "path", name = "uk_permisos_path")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permiso {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String path;

  @ManyToOne
  @JoinColumn(name = "rol_id", nullable = false)
  private UsuarioRol rol;

  public static class PermisoBuilder {
  }
}
