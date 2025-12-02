package com.example.simplemvc.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.simplemvc.dto.TopProductoDTO;
import com.example.simplemvc.dto.TopVendedorDTO;
import com.example.simplemvc.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {
  @Query("SELECT MAX(v.numeroComprobante) FROM Venta v WHERE v.tipoComprobante = :tipo")
  String obtenerUltimoNumeroPorTipo(@Param("tipo") String tipo);

  List<Venta> findByFechaVentaBetweenOrderByFechaVentaDesc(Timestamp inicio, Timestamp fin);

  @Query("SELECT " +
      "d.producto.nombre AS nombreProducto, " +
      "d.producto.codigo AS codigoProducto, " +
      "SUM(d.cantidad) AS cantidadTotal, " +
      "SUM(d.subtotal) AS totalVendido " +
      "FROM VentaDetalle d " +
      "JOIN d.venta v " +
      "WHERE v.fechaVenta BETWEEN :inicio AND :fin " +
      "AND v.estado = 'COMPLETADA' " +
      "GROUP BY d.producto.idProducto, d.producto.nombre, d.producto.codigo " +
      "ORDER BY totalVendido DESC")
  List<TopProductoDTO> obtenerTopProductos(@Param("inicio") Timestamp inicio, @Param("fin") Timestamp fin);

  @Query("SELECT " +
      "CONCAT(p.nombres, ' ', p.apellidos) AS nombreVendedor, " + // O u.nombreUsuario
      "COUNT(v) AS cantidadVentas, " +
      "SUM(v.total) AS totalVendido " +
      "FROM Venta v " +
      "JOIN v.vendedor u " +
      "JOIN u.persona p " +
      "WHERE v.fechaVenta BETWEEN :inicio AND :fin " +
      "AND v.estado = 'COMPLETADA' " +
      "GROUP BY u.id, p.nombres, p.apellidos " +
      "ORDER BY totalVendido DESC")
  List<TopVendedorDTO> obtenerRankingVendedores(@Param("inicio") Timestamp inicio, @Param("fin") Timestamp fin);
}