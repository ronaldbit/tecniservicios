package com.example.simplemvc.model;

import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "usuario", uniqueConstraints = {
    @UniqueConstraint(columnNames = "correo", name = "uk_usuario_correo")
})
@SQLDelete(sql = "UPDATE usuario SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
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

  @Column(columnDefinition = "varchar(254)", nullable = false)
  private String correo;

  @Column(columnDefinition = "varchar(255)", nullable = false)
  private String password;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "rol_id", nullable = false)
  private UsuarioRol rol;

  @Builder.Default
  private boolean deleted = false;

  @Override
  public String getUsername() {
    return correo;
  }

  @Override
  public List<? extends GrantedAuthority> getAuthorities() {
    if (rol == null || rol.getNombre() == null) {
      return List.of();
    }
    return List.of(new SimpleGrantedAuthority(rol.getNombre()));
  }
}
