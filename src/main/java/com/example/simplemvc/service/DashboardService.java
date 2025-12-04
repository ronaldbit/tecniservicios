package com.example.simplemvc.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.simplemvc.dto.DashboardVentasDto;
import com.example.simplemvc.model.enums.CanalVenta;
import com.example.simplemvc.model.enums.EstadoPedido;
import com.example.simplemvc.repository.PedidoProveedorRepository;
import com.example.simplemvc.repository.VentaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

  private final VentaRepository ventaRepository;
  private final PedidoProveedorRepository pedidoRepository;

  public DashboardVentasDto obtenerMetricas() {
    DashboardVentasDto dto = new DashboardVentasDto();
    LocalDate hoy = LocalDate.now();
    Timestamp inicioHoy = Timestamp.valueOf(hoy.atStartOfDay());
    Timestamp finHoy = Timestamp.valueOf(hoy.atTime(LocalTime.MAX));
    Timestamp inicioAyer = Timestamp.valueOf(hoy.minusDays(1).atStartOfDay());
    Timestamp finAyer = Timestamp.valueOf(hoy.minusDays(1).atTime(LocalTime.MAX));
    dto.setIngresosHoy(ventaRepository.sumarVentasEnRango(inicioHoy, finHoy));
    dto.setIngresosAyer(ventaRepository.sumarVentasEnRango(inicioAyer, finAyer));
    dto.setOrdenesPendientes(pedidoRepository.countByEstado(EstadoPedido.PENDIENTE));
    Timestamp inicioMes = Timestamp.valueOf(hoy.withDayOfMonth(1).atStartOfDay());
    dto.setDevolucionesMes(ventaRepository.contarVentasAnuladasDesde(inicioMes));
    dto.setIngresosTiendaFisica(ventaRepository.sumarVentasPorCanal(CanalVenta.TIENDA_FISICA));
    dto.setIngresosTiendaOnline(ventaRepository.sumarVentasPorCanal(CanalVenta.TIENDA_ONLINE));
    dto.setIngresosCreditos(BigDecimal.ZERO);
    dto.setIngresosOtros(BigDecimal.ZERO);

    return dto;
  }

  public List<BigDecimal> obtenerGraficoAnual() {
    List<Object[]> resultados = ventaRepository.obtenerVentasPorMesAnioActual();
    BigDecimal[] totalesPorMes = new BigDecimal[12];
    Arrays.fill(totalesPorMes, BigDecimal.ZERO);
    for (Object[] fila : resultados) {
      int mes = ((Number) fila[0]).intValue();
      BigDecimal total = (BigDecimal) fila[1];
      if (mes >= 1 && mes <= 12) {
        totalesPorMes[mes - 1] = total;
      }
    }
    return Arrays.asList(totalesPorMes);
  }
}