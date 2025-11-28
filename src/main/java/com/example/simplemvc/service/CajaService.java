package com.example.simplemvc.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.simplemvc.model.Caja;
import com.example.simplemvc.model.CajaSesion;
import com.example.simplemvc.model.MovimientoCaja;
import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.model.enums.EstadoCaja;
import com.example.simplemvc.model.enums.TipoMovimiento;
import com.example.simplemvc.repository.CajaRepository;
import com.example.simplemvc.repository.MovimientoCajaRepository;
import com.example.simplemvc.request.AbrirCajaRequest;
import com.example.simplemvc.request.CerrarCajaRequest;
import com.example.simplemvc.request.MovimientoCajaRequest;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CajaService {

  private final CajaRepository cajaRepository;
  private final CajaSesionRepository cajaSesionRepository;
  private final MovimientoCajaRepository movimientoCajaRepository;
  private final GetActualUsuarioService getActualUsuarioService;

  @Transactional
  public void abrirCaja(AbrirCajaRequest request) {
    Usuario usuario = getActualUsuarioService.get();

    Optional<CajaSesion> sesionActiva = cajaSesionRepository.findByCaja_IdAndEstado(request.getCajaId(),
        EstadoCaja.ABIERTA);
    if (sesionActiva.isPresent()) {
      throw new IllegalStateException("La caja ya se encuentra abierta.");
    }

    Caja caja = cajaRepository.findById(request.getCajaId())
        .orElseThrow(() -> new EntityNotFoundException("Caja no encontrada"));

    CajaSesion sesion = CajaSesion.builder()
        .caja(caja)
        .usuarioApertura(usuario)
        .fechaApertura(LocalDateTime.now())
        .montoInicial(request.getMontoInicial())
        .estado(EstadoCaja.ABIERTA)
        .totalIngresos(BigDecimal.ZERO)
        .totalEgresos(BigDecimal.ZERO)
        .build();

    caja.setEstadoActual(EstadoCaja.ABIERTA);
    cajaRepository.save(caja);
    CajaSesion guardada = cajaSesionRepository.save(sesion);

    registrarMovimientoInterno(guardada, TipoMovimiento.APERTURA, request.getMontoInicial(),
        "Apertura de caja: " + request.getNotas());
  }

  @Transactional
  public void registrarMovimientoManual(MovimientoCajaRequest request) {

    CajaSesion sesion = obtenerSesionAbierta(1L);

    TipoMovimiento tipo = TipoMovimiento.valueOf(request.getTipo());
    registrarMovimientoInterno(sesion, tipo, request.getMonto(), request.getMotivo());
  }

  @Transactional
  public void registrarIngresoVenta(BigDecimal monto, String nroComprobante) {
    try {
      CajaSesion sesion = obtenerSesionAbierta(1L);

      registrarMovimientoInterno(sesion,
          TipoMovimiento.INGRESO,
          monto,
          "Venta registrada: " + nroComprobante);

    } catch (IllegalStateException e) {
      log.warn("Venta registrada sin caja abierta: {}", nroComprobante);
    }
  }

  @Transactional
  public void cerrarCaja(CerrarCajaRequest request) {
    CajaSesion sesion = obtenerSesionAbierta(1L);

    sesion.setFechaCierre(LocalDateTime.now());
    sesion.setMontoFinal(request.getMontoFinal());
    sesion.setEstado(EstadoCaja.CERRADA);

    sesion.getCaja().setEstadoActual(EstadoCaja.CERRADA);
    cajaRepository.save(sesion.getCaja());

    registrarMovimientoInterno(sesion, TipoMovimiento.CIERRE, request.getMontoFinal(), "Cierre de caja");
    cajaSesionRepository.save(sesion);
  }

  private void registrarMovimientoInterno(CajaSesion sesion, TipoMovimiento tipo, BigDecimal monto,
      String descripcion) {
    MovimientoCaja mov = MovimientoCaja.builder()
        .sesion(sesion)
        .usuario(getActualUsuarioService.get())
        .tipo(tipo)
        .monto(monto)
        .descripcion(descripcion)
        .fecha(LocalDateTime.now())
        .build();

    movimientoCajaRepository.save(mov);

    if (tipo == TipoMovimiento.INGRESO) {
      sesion.setTotalIngresos(sesion.getTotalIngresos().add(monto));
    } else if (tipo == TipoMovimiento.EGRESO) {
      sesion.setTotalEgresos(sesion.getTotalEgresos().add(monto));
    }
    cajaSesionRepository.save(sesion);
  }

  public CajaSesion obtenerSesionAbierta(Long cajaId) {
    return cajaSesionRepository.findByCaja_IdAndEstado(cajaId, EstadoCaja.ABIERTA)
        .orElseThrow(() -> new IllegalStateException("No hay una sesión abierta en esta caja."));
  }

  public Optional<CajaSesion> obtenerSesionActualOpcional(Long cajaId) {
    return cajaSesionRepository.findByCaja_IdAndEstado(cajaId, EstadoCaja.ABIERTA);
  }

  public List<MovimientoCaja> listarMovimientosSesionActual(Long cajaId) {
    CajaSesion sesion = obtenerSesionAbierta(cajaId);
    return movimientoCajaRepository.findBySesion_IdOrderByFechaDesc(sesion.getId());
  }
}
