package com.example.simplemvc.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.simplemvc.model.CajaSesion;
import com.example.simplemvc.model.MovimientoCaja;
import com.example.simplemvc.repository.MovimientoCajaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportesService {

  private final CajaSesionRepository cajaSesionRepository;
  private final MovimientoCajaRepository movimientoCajaRepository;

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
}
