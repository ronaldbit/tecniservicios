package com.example.simplemvc.repository;

import com.example.simplemvc.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    @Query("SELECT MAX(v.numeroComprobante) FROM Venta v WHERE v.tipoComprobante = :tipo")
    String obtenerUltimoNumeroPorTipo(@Param("tipo") String tipo);
}