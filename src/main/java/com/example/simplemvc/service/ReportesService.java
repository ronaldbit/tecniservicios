package com.example.simplemvc.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.simplemvc.dto.TopProductoDTO;
import com.example.simplemvc.dto.TopVendedorDTO;
import com.example.simplemvc.model.CajaSesion;
import com.example.simplemvc.model.MovimientoCaja;
import com.example.simplemvc.model.Producto;
import com.example.simplemvc.model.Venta;
import com.example.simplemvc.model.enums.EstadoVenta;
import com.example.simplemvc.repository.MovimientoCajaRepository;
import com.example.simplemvc.repository.ProductoRepository;
import com.example.simplemvc.repository.VentaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportesService {

  private final CajaSesionRepository cajaSesionRepository;
  private final MovimientoCajaRepository movimientoCajaRepository;
  private final ProductoRepository productoRepository;
  private final VentaRepository ventaRepository;

  public List<CajaSesion> buscarSesionesPorFecha(Long cajaId, LocalDate fecha) {
    LocalDateTime inicioDia = fecha.atStartOfDay();
    LocalDateTime finDia = fecha.atTime(LocalTime.MAX);
    return cajaSesionRepository.findByCaja_IdAndFechaAperturaBetweenOrderByFechaAperturaDesc(
        cajaId, inicioDia, finDia);
  }

  public Map<String, Object> obtenerDatosReporteCaja(Long sesionId) {
    CajaSesion sesion = cajaSesionRepository.findById(sesionId)
        .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

    List<MovimientoCaja> movimientos = movimientoCajaRepository.findBySesion_IdOrderByFechaDesc(sesionId);

    java.math.BigDecimal saldoCalculado = sesion.getMontoInicial()
        .add(sesion.getTotalIngresos())
        .subtract(sesion.getTotalEgresos());

    Map<String, Object> datos = new HashMap<>();
    datos.put("sesion", sesion);
    datos.put("movimientos", movimientos);
    datos.put("saldoCalculado", saldoCalculado);

    return datos;
  }

  public List<Producto> obtenerReporteStockBajo() {
    return productoRepository.findProductosStockBajo();
  }

  public Map<String, Object> obtenerValorizacionInventario() {
    List<Producto> productos = productoRepository.findAll();
    BigDecimal totalValor = BigDecimal.ZERO;
    int totalItems = 0;
    for (Producto p : productos) {
      BigDecimal valorProducto = p.getPrecio().multiply(p.getStockActual());
      totalValor = totalValor.add(valorProducto);
      totalItems += p.getStockActual().intValue();
    }
    Map<String, Object> datos = new HashMap<>();
    datos.put("totalValor", totalValor);
    datos.put("totalItems", totalItems);
    datos.put("productos", productos);
    return datos;
  }

  // REPORTE DE VENTAS POR FECHA
  @Transactional(readOnly = true)
  public Map<String, Object> obtenerReporteVentas(LocalDate fechaInicio, LocalDate fechaFin, Long usuarioId) {

    Timestamp inicio = Timestamp.valueOf(fechaInicio.atStartOfDay());
    Timestamp fin = Timestamp.valueOf(fechaFin.atTime(LocalTime.MAX));
    List<Venta> ventas;
    if (usuarioId != null && usuarioId > 0) {
      ventas = ventaRepository.findByFechaVentaBetweenAndVendedor_IdOrderByFechaVentaDesc(inicio, fin, usuarioId);
    } else {
      ventas = ventaRepository.findByFechaVentaBetweenOrderByFechaVentaDesc(inicio, fin);
    }
    BigDecimal totalIngresos = BigDecimal.ZERO;
    int cantidadVentas = 0;
    int ventasAnuladas = 0;
    for (Venta v : ventas) {
      if (v.getEstado() == EstadoVenta.COMPLETADA) {
        totalIngresos = totalIngresos.add(v.getTotal());
        cantidadVentas++;
      } else if (v.getEstado() == EstadoVenta.ANULADA) {
        ventasAnuladas++;
      }
    }
    Map<String, Object> datos = new HashMap<>();
    datos.put("ventas", ventas);
    datos.put("fechaInicio", fechaInicio);
    datos.put("fechaFin", fechaFin);
    datos.put("totalIngresos", totalIngresos);
    datos.put("cantidadVentas", cantidadVentas);
    datos.put("ventasAnuladas", ventasAnuladas);
    if (usuarioId != null && usuarioId > 0 && !ventas.isEmpty()) {
      datos.put("nombreVendedor", ventas.get(0).getVendedor().getPersona().getNombres()); // Ajusta según tu modelo
    } else {
      datos.put("nombreVendedor", "Todos");
    }
    return datos;
  }

  // REPORTE: TOP PRODUCTOS
  public Map<String, Object> obtenerReporteTopProductos(LocalDate fechaInicio, LocalDate fechaFin) {
    Timestamp inicio = Timestamp.valueOf(fechaInicio.atStartOfDay());
    Timestamp fin = Timestamp.valueOf(fechaFin.atTime(LocalTime.MAX));

    List<TopProductoDTO> topProductos = ventaRepository.obtenerTopProductos(inicio, fin);
    BigDecimal granTotalVendido = topProductos.stream()
        .map(TopProductoDTO::getTotalVendido)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalUnidades = topProductos.stream()
        .map(TopProductoDTO::getCantidadTotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    Map<String, Object> datos = new HashMap<>();
    datos.put("productos", topProductos);
    datos.put("fechaInicio", fechaInicio);
    datos.put("fechaFin", fechaFin);
    datos.put("granTotalVendido", granTotalVendido);
    datos.put("totalUnidades", totalUnidades);

    return datos;
  }

  // REPORTE: RANKING VENDEDORES
  public Map<String, Object> obtenerRankingVendedores(LocalDate fechaInicio, LocalDate fechaFin) {
    Timestamp inicio = Timestamp.valueOf(fechaInicio.atStartOfDay());
    Timestamp fin = Timestamp.valueOf(fechaFin.atTime(LocalTime.MAX));
    List<TopVendedorDTO> ranking = ventaRepository.obtenerRankingVendedores(inicio, fin);
    BigDecimal totalGeneral = ranking.stream()
        .map(TopVendedorDTO::getTotalVendido)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    long totalOperaciones = ranking.stream()
        .mapToLong(TopVendedorDTO::getCantidadVentas)
        .sum();

    Map<String, Object> datos = new HashMap<>();
    datos.put("vendedores", ranking);
    datos.put("fechaInicio", fechaInicio);
    datos.put("fechaFin", fechaFin);
    datos.put("totalGeneral", totalGeneral);
    datos.put("totalOperaciones", totalOperaciones);

    return datos;
  }

}
