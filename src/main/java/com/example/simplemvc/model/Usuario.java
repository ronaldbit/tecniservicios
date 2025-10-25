package com.example.simplemvc.model;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.simplemvc.shared.utils.converter.ListUsuarioRolAttributeConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Audited
@Table(name = "usuario", uniqueConstraints = {
    @UniqueConstraint(columnNames = "correo", name = "uk_usuario_correo")
})
@SQLDelete(sql = "UPDATE usuario SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@Getter
@Builder
@ToString(exclude = "password")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "persona_id", nullable = false)
  private Persona persona;

  @Column(columnDefinition = "varchar(254)", nullable = false)
  private String correo;

  @Column(columnDefinition = "varchar(255)", nullable = false)
  private String password;

  @Convert(converter = ListUsuarioRolAttributeConverter.class)
  private List<UsuarioRol> roles;

  @Builder.Default
  private boolean deleted = false;

  @Override
  public String getUsername() {
    return correo;
  }

  @Override
  public List<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }
}
