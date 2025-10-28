package com.example.simplemvc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(
    name = "rol",
    uniqueConstraints = {
      @jakarta.persistence.UniqueConstraint(columnNames = "nombre", name = "rol_nombre")
    })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rol implements GrantedAuthority {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "varchar(80)", nullable = false)
  private String nombre;

  @Column(columnDefinition = "varchar(200)", nullable = true)
  private String descripcion;

  @Builder.Default
  @OneToMany(mappedBy = "rol", fetch = FetchType.EAGER)
  private List<Permiso> permisos = new ArrayList<>();

  @Override
  public String getAuthority() {
    return "ROLE_" + nombre.toUpperCase();
  }
}
