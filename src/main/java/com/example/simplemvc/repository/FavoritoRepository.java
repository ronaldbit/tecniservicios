package com.example.simplemvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.simplemvc.model.Favorito;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    // LISTAR FAVORITOS POR PERSONA (para listar en perfil/favoritos)
    @Query("SELECT f FROM Favorito f WHERE f.persona.id = :personaId")
    List<Favorito> findByPersonaId(@Param("personaId") Long personaId);

    // SABER SI UN PRODUCTO ES FAVORITO DE ESA PERSONA
    @Query("""
           SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END
           FROM Favorito f
           WHERE f.persona.id = :personaId
           AND f.producto.idProducto = :productoId
           """)
    boolean existsByPersonaIdAndProductoId(
            @Param("personaId") Long personaId,
            @Param("productoId") Long productoId);

    // ELIMINAR FAVORITO (toggle: si existe, borrar)
    @Modifying
    @Transactional
    @Query("""
           DELETE FROM Favorito f
           WHERE f.persona.id = :personaId
           AND f.producto.idProducto = :productoId
           """)
    void deleteByPersonaIdAndProductoId(
            @Param("personaId") Long personaId,
            @Param("productoId") Long productoId);
}
