package com.example.simplemvc.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.simplemvc.dto.TopProductoDTO;
import com.example.simplemvc.dto.TopVendedorDTO;
import com.example.simplemvc.model.Venta;
import com.example.simplemvc.model.enums.CanalVenta;

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
      "CONCAT(p.nombres, ' ', p.apellidos) AS nombreVendedor, " +
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

  @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.fechaVenta BETWEEN :inicio AND :fin AND v.estado = 'COMPLETADA'")
  BigDecimal sumarVentasEnRango(@Param("inicio") Timestamp inicio, @Param("fin") Timestamp fin);

  @Query("SELECT COUNT(v) FROM Venta v WHERE v.fechaVenta >= :fecha AND v.estado = 'ANULADA'")
  long contarVentasAnuladasDesde(@Param("fecha") Timestamp fecha);

  @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.canalVenta = :canal AND v.estado = 'COMPLETADA'")
  BigDecimal sumarVentasPorCanal(@Param("canal") CanalVenta canal);

  @Query(value = "SELECT MONTH(fecha_venta) as mes, SUM(total) as total " +
      "FROM ventas " +
      "WHERE YEAR(fecha_venta) = YEAR(CURRENT_DATE()) " +
      "AND estado_venta = 'COMPLETADA' " +
      "GROUP BY MONTH(fecha_venta) " +
      "ORDER BY mes ASC", nativeQuery = true)
  List<Object[]> obtenerVentasPorMesAnioActual();

}