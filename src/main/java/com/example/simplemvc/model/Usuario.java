package com.example.simplemvc.model;

import com.example.simplemvc.model.enums.EstadoEntidad;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(
    name = "usuario",
    uniqueConstraints = {@UniqueConstraint(columnNames = "correo", name = "uk_usuario_correo")})
@SQLDelete(sql = "UPDATE usuario SET estado = 0 WHERE id = ?")
@SQLRestriction("estado <> 0")
@Getter
@Setter
@Builder
@ToString(exclude = "password")
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Usuario implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "persona_id", nullable = false)
  private Persona persona;

  @ManyToOne
  @JoinColumn(name = "sucursal_id", nullable = false)
  private Sucursal sucursal;

  @Column(columnDefinition = "varchar(100)", nullable = false)
  private String nombreUsuario;

  @Column(columnDefinition = "varchar(255)", nullable = false)
  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "usuario_rol",
      joinColumns = @JoinColumn(name = "usuario_id"),
      inverseJoinColumns = @JoinColumn(name = "rol_id"))
  @Builder.Default
  private List<Rol> roles = new ArrayList<>();

  @Builder.Default
  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private EstadoEntidad estado = EstadoEntidad.ACTIVO;

  @Builder.Default
  @Column(nullable = false)
  private LocalDateTime fechaCreacion = LocalDateTime.now();

  @Builder.Default
  @Column(nullable = false)
  private LocalDateTime fechaActualizacion = LocalDateTime.now();

  @Override
  public String getUsername() {
    return nombreUsuario;
  }

  @Override
  public List<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }
}
