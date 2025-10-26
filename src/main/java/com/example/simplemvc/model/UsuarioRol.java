package com.example.simplemvc.model;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_rol", uniqueConstraints = {
    @UniqueConstraint(columnNames = "nombre", name = "uk_usuario_rol_nombre")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRol implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, unique = true, length = 50)
  private String nombre;
  @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Permiso> permisos;
  @Override
  public String getAuthority() {
    return "ROLE_" + this.nombre.toUpperCase();
  }
}
