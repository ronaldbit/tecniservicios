package com.example.simplemvc.security;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class DbUserDetailsService implements UserDetailsService {

  private final JdbcTemplate jdbc;

  public DbUserDetailsService(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {

      var u =
          jdbc.queryForMap(
              """
					  SELECT id, usuario AS username, password AS password_hash, activo
					  FROM usuario
					  WHERE usuario = ? OR email = ?
					""",
              username,
              username);

      boolean enabled =
          u.get("activo") == null || Integer.valueOf(String.valueOf(u.get("activo"))) == 1;

      List<String> roles =
          jdbc.queryForList(
              """
					  SELECT r.nombre
					  FROM usuario_rol ur
					  JOIN rol r ON r.id = ur.rol_id
					  WHERE ur.usuario_id = ?
					""",
              String.class,
              u.get("id"));

      List<GrantedAuthority> auths =
          roles.stream()
              .map(r -> new SimpleGrantedAuthority("ROLE_" + r.trim().toUpperCase()))
              .collect(Collectors.toList());

      return User.builder()
          .username((String) u.get("username"))
          .password((String) u.get("password_hash")) // BCrypt
          // en
          // DB
          .authorities(auths)
          .disabled(!enabled)
          .build();

    } catch (EmptyResultDataAccessException e) {
      throw new UsernameNotFoundException("No existe: " + username);
    }
  }
}
