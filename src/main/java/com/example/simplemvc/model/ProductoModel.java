package com.example.simplemvc.model;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class ProductoModel {

  @Autowired
  private JdbcTemplate jdbc;

  private final RowMapper<Producto> mapper = new RowMapper<>() {
    @Override
    public Producto mapRow(ResultSet rs, int rowNum) throws SQLException {
      Producto p = new Producto();
      p.setId(rs.getLong("id_producto"));
      try { p.setCodigo(rs.getString("codigo")); } catch(Exception ignore){}
      try { p.setNombre(rs.getString("nombre")); } catch(Exception ignore){}
      try { p.setPrecio((Double)rs.getObject("precio")); } catch(Exception ignore){}
      try { p.setPublicarOnline((Boolean)rs.getObject("publicar_online")); } catch(Exception ignore){}
      return p;
    }
  };


  public List<Producto> listarPublico(String q){
    if(q==null || q.isBlank()){
      return jdbc.query("SELECT id_producto, codigo, nombre, precio, publicar_online FROM productos WHERE publicar_online IS NULL OR publicar_online=1 ORDER BY id_producto DESC", mapper);
    }
    String like = "%" + q.toLowerCase() + "%";
    return jdbc.query("SELECT id_producto, codigo, nombre, precio, publicar_online FROM productos WHERE (publicar_online IS NULL OR publicar_online=1) AND LOWER(nombre) LIKE ?", mapper, like);
  }

  public Optional<Producto> obtener(Long id){
    List<Producto> list = jdbc.query("SELECT id_producto, codigo, nombre, precio, publicar_online FROM productos WHERE id_producto=?", mapper, id);
    return list.isEmpty()? Optional.empty() : Optional.of(list.get(0));
  }

  
  public Long crear(Producto p){
    jdbc.update("INSERT INTO productos (codigo, nombre, precio, publicar_online) VALUES (?,?,?,?)",
    p.getCodigo(), p.getNombre(), p.getPrecio(), p.getPublicarOnline());
    Long id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    return id;
  }

  public int actualizar(Long id, Producto p){
    return jdbc.update("UPDATE productos SET codigo=?, nombre=?, precio=?, publicar_online=? WHERE id_producto=?",
      p.getCodigo(), p.getNombre(), p.getPrecio(), p.getPublicarOnline(), id);
  }

  public int eliminar(Long id){
    return jdbc.update("DELETE FROM productos WHERE id_producto=?", id);
  }
}
