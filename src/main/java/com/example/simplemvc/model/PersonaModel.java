package com.example.simplemvc.model;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PersonaModel {
    private final JdbcTemplate jdbcTemplate;

    public PersonaModel(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class PersonaMapper implements RowMapper<Persona> {
        @Override
        public Persona mapRow(ResultSet rs, int rowNum) throws SQLException {
            Persona p = new Persona();
            p.setId_persona(rs.getLong("id_persona"));
            p.setId_tipo_documento(rs.getInt("id_tipo_documento"));
            p.setNro_doc(rs.getString("nro_doc"));
            p.setTipo_persona(rs.getString("tipo_persona"));
            p.setNombres(rs.getString("nombres"));
            p.setApellidos(rs.getString("apellidos"));
            p.setRazon_social(rs.getString("razon_social"));
            p.setEmail(rs.getString("email"));
            p.setTelefono(rs.getString("telefono"));
            p.setDireccion(rs.getString("direccion"));
            return p;
        }
    }

    public List<Persona> listar() {
        String sql = "SELECT id_persona, id_tipo_documento, nro_doc, tipo_persona, nombres, apellidos, razon_social, email, telefono, direccion FROM personas";
        return jdbcTemplate.query(sql, new PersonaMapper());
    }


    public Long crear(Persona p) {
        String sql = """
            INSERT INTO personas (id_tipo_documento, nro_doc, tipo_persona, nombres, apellidos, razon_social, email, telefono, direccion)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql,
            p.getId_tipo_documento(),
            p.getNro_doc(),
            p.getTipo_persona(),
            p.getNombres(),
            p.getApellidos(),
            p.getRazon_social(),
            p.getEmail(),
            p.getTelefono(),
            p.getDireccion()
        );
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    public int actualizar(Long id, Persona p) {
        String sql = """
            UPDATE personas SET 
              id_tipo_documento=?, nro_doc=?, tipo_persona=?, nombres=?, apellidos=?, 
              razon_social=?, email=?, telefono=?, direccion=? 
            WHERE id_persona=?
        """;
        return jdbcTemplate.update(sql,
            p.getId_tipo_documento(),
            p.getNro_doc(),
            p.getTipo_persona(),
            p.getNombres(),
            p.getApellidos(),
            p.getRazon_social(),
            p.getEmail(),
            p.getTelefono(),
            p.getDireccion(),
            id
        );
    }
 
    public int eliminar(Long id) {
        String sql = "DELETE FROM personas WHERE id_persona=?";
        return jdbcTemplate.update(sql, id);
    }
    public Persona buscarPorId(Long id) {
        String sql = "SELECT * FROM personas WHERE id_persona=?";
        List<Persona> lista = jdbcTemplate.query(sql, new PersonaMapper(), id);
        return lista.isEmpty() ? null : lista.get(0);
    }
}
