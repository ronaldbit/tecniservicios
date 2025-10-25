package com.example.simplemvc.model;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UsuarioModel {

    @Autowired
    private JdbcTemplate jdbc;

    private final RowMapper<Usuario> mapper = new RowMapper<>() {
        @Override
        public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
            Usuario u = new Usuario();
            u.setIdUsuario(rs.getLong("id_usuario"));
            u.setIdPersona(rs.getObject("id_persona") != null ? rs.getLong("id_persona") : null);
            u.setIdSucursal(rs.getObject("id_sucursal") != null ? rs.getLong("id_sucursal") : null);
            u.setNombreUsuario(rs.getString("nombre_usuario"));
            u.setHashPass(rs.getString("hash_pass"));
            u.setActivo(rs.getBoolean("activo"));
            u.setIdRol(rs.getObject("id_rol") != null ? rs.getLong("id_rol") : null);
            Timestamp created = rs.getTimestamp("created_at");
            Timestamp updated = rs.getTimestamp("updated_at");
            if (created != null)
                u.setCreatedAt(created.toLocalDateTime());
            if (updated != null)
                u.setUpdatedAt(updated.toLocalDateTime());
            return u;
        }
    };

    public List<Usuario> listar() {
        String sql = """
                SELECT u.id_usuario, u.id_persona, u.id_sucursal, u.nombre_usuario,
                       u.hash_pass, u.activo, u.created_at, u.updated_at, ur.id_rol
                FROM usuarios u
                LEFT JOIN usuario_rol ur ON u.id_usuario = ur.id_usuario
                ORDER BY u.id_usuario DESC
                """;
        return jdbc.query(sql, mapper);
    }

    public Optional<Usuario> obtener(Long id) {
        String sql = """
                SELECT u.id_usuario, u.id_persona, u.id_sucursal, u.nombre_usuario,
                       u.hash_pass, u.activo, u.created_at, u.updated_at, ur.id_rol
                FROM usuarios u
                LEFT JOIN usuario_rol ur ON u.id_usuario = ur.id_usuario
                WHERE u.id_usuario = ?
                """;
        List<Usuario> lista = jdbc.query(sql, mapper, id);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    public int crear(Usuario u) {
        return jdbc.update(
                """
                            INSERT INTO usuarios (id_persona, id_sucursal, nombre_usuario, hash_pass, activo, created_at, updated_at)
                            VALUES (?, ?, ?, ?, ?, current_timestamp(), current_timestamp())
                        """,
                u.getIdPersona(), u.getIdSucursal(), u.getNombreUsuario(), u.getHashPass(), u.getActivo());
    }

    public int asignarRol(Long idUsuario, Long idRol) {
        return jdbc.update("""
                    INSERT INTO usuario_rol (id_usuario, id_rol) VALUES (?, ?)
                """, idUsuario, idRol);
    }

    public int actualizar(Long id, Usuario u) {
        return jdbc.update(
                """
                            UPDATE usuarios
                            SET id_persona=?, id_sucursal=?, nombre_usuario=?, hash_pass=?, activo=?, updated_at=current_timestamp()
                            WHERE id_usuario=?
                        """,
                u.getIdPersona(), u.getIdSucursal(), u.getNombreUsuario(), u.getHashPass(), u.getActivo(), id);
    }

    public int eliminar(Long id) {
        jdbc.update("DELETE FROM usuario_rol WHERE id_usuario=?", id);
        return jdbc.update("DELETE FROM usuarios WHERE id_usuario=?", id);
    }

    public int crearRol(String nombreRol, String descripcion) {
        String sql = "INSERT INTO roles (nombre, descripcion) VALUES (?, ?)";
        return jdbc.update(sql, nombreRol, descripcion);
    }

    public List<Map<String, Object>> listarRoles() {
        String sql = "SELECT id_rol, nombre, descripcion FROM roles ORDER BY id_rol ASC";
        return jdbc.queryForList(sql);
    }

}
