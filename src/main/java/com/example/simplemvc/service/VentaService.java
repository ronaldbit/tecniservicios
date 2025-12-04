package com.example.simplemvc.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.simplemvc.dto.VentaDto;
import com.example.simplemvc.model.Producto;
import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.model.Venta;
import com.example.simplemvc.model.VentaDetalle;
import com.example.simplemvc.model.VentaMapper;
import com.example.simplemvc.model.enums.CanalVenta;
import com.example.simplemvc.model.enums.EstadoVenta;
import com.example.simplemvc.model.enums.MetodoPago;
import com.example.simplemvc.repository.ProductoRepository;
import com.example.simplemvc.repository.VentaRepository;
import com.example.simplemvc.request.CheckoutItemRequest;
import com.example.simplemvc.request.CheckoutRequest;
import com.example.simplemvc.request.CrearVentaDetalleRequest;
import com.example.simplemvc.request.CrearVentaRequest;
import com.example.simplemvc.shared.Exeption.StockInsuficienteException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaService {

  private final VentaRepository ventaRepository;
  private final ProductoRepository productoRepository;
  private final VentaMapper ventaMapper;
  private final CajaService cajaService;

  private final GetActualUsuarioService getActualUsuarioService;

  private static final BigDecimal TASA_IGV = new BigDecimal("0.18");
  private static final BigDecimal UNO_MAS_IGV = new BigDecimal("1.18");

  @Transactional
  public VentaDto crearVenta(CrearVentaRequest request) {

    Usuario vendedor = getActualUsuarioService.get();
    if (vendedor == null) {
      throw new IllegalStateException("No se pudo identificar al vendedor. El usuario no está autenticado.");
    }
    if (request.getNumeroComprobante() == null || request.getNumeroComprobante().isEmpty()) {
      String nuevoNumero = generarSiguienteNumero(request.getTipoComprobante());
      request.setNumeroComprobante(nuevoNumero);
    }

    Venta venta = new Venta();
    venta.setVendedor(vendedor);
    venta.setEstado(EstadoVenta.COMPLETADA);
    venta.setClienteTipoDocumento(request.getClienteTipoDocumento());
    venta.setClienteNumeroDocumento(request.getClienteNumeroDocumento());
    venta.setClienteNombreCompleto(request.getClienteNombreCompleto());
    venta.setClienteDireccion(request.getClienteDireccion());
    venta.setTipoComprobante(request.getTipoComprobante());
    venta.setSerieComprobante(request.getSerieComprobante());
    venta.setNumeroComprobante(request.getNumeroComprobante());
    venta.setMetodoPago(request.getMetodoPago());
    List<VentaDetalle> detallesList = new ArrayList<>();
    BigDecimal totalAcumulado = BigDecimal.ZERO;
    for (CrearVentaDetalleRequest detRequest : request.getDetalles()) {
      Producto producto = productoRepository.findById(detRequest.getIdProducto())
          .orElseThrow(() -> new EntityNotFoundException(
              "Producto no encontrado con ID: " + detRequest.getIdProducto()));
      BigDecimal cantidadVendida = detRequest.getCantidad();
      if (producto.getStockActual().compareTo(cantidadVendida) < 0) {
        throw new StockInsuficienteException(
            "Stock insuficiente para el producto: " + producto.getNombre() +
                ". Solicitado: " + cantidadVendida + ", Disponible: " + producto.getStockActual());
      }
      producto.setStockActual(producto.getStockActual().subtract(cantidadVendida));
      productoRepository.save(producto);
      VentaDetalle detalle = new VentaDetalle();
      detalle.setProducto(producto);
      detalle.setCantidad(cantidadVendida);
      BigDecimal precioUnit = detRequest.getPrecioUnitario();
      BigDecimal descuentoUnit = (detRequest.getDescuentoUnitario() != null) ? detRequest.getDescuentoUnitario()
          : BigDecimal.ZERO;
      detalle.setPrecioUnitario(precioUnit);
      detalle.setDescuentoUnitario(descuentoUnit);
      BigDecimal precioFinal = precioUnit.subtract(descuentoUnit);
      BigDecimal subtotalLinea = precioFinal.multiply(cantidadVendida);
      detalle.setSubtotal(subtotalLinea);
      totalAcumulado = totalAcumulado.add(subtotalLinea);
      detalle.setVenta(venta);
      detallesList.add(detalle);
    }

    venta.setDetalles(detallesList);
    BigDecimal totalVenta = totalAcumulado.setScale(2, RoundingMode.HALF_UP);
    BigDecimal subtotalVenta = totalVenta.divide(UNO_MAS_IGV, 2, RoundingMode.HALF_UP);
    BigDecimal igvVenta = totalVenta.subtract(subtotalVenta);
    venta.setTotal(totalVenta);
    venta.setSubtotal(subtotalVenta);
    venta.setIgv(igvVenta);
    venta.setCanalVenta(CanalVenta.TIENDA_FISICA);
    Venta ventaGuardada = ventaRepository.save(venta);

    cajaService.registrarIngresoVenta(
        ventaGuardada.getTotal(),
        ventaGuardada.getSerieComprobante() + "-" + ventaGuardada.getNumeroComprobante(),
        ventaGuardada.getMetodoPago());
    return ventaMapper.toDto(ventaGuardada);
  }

  @Transactional
  public Venta crearVentaOnlineDesdeCheckout(
      CheckoutRequest request,
      String orderId) {

    Venta venta = new Venta();

    // si no tienes PENDIENTE en tu enum, temporalmente usa COMPLETADA
    venta.setEstado(EstadoVenta.PENDIENTE);
    venta.setCanalVenta(CanalVenta.TIENDA_ONLINE);
    venta.setMetodoPago(MetodoPago.PAYSHOP);

    String nombreCompleto = (request.getNombres() != null ? request.getNombres() : "") +
        " " +
        (request.getApellidos() != null ? request.getApellidos() : "");
    venta.setClienteNombreCompleto(nombreCompleto.trim());
    venta.setClienteDireccion(request.getDireccion());

    venta.setTipoComprobante("TICKET");
    venta.setSerieComprobante("WEB");
    venta.setNumeroComprobante(orderId);

    List<VentaDetalle> detallesList = new ArrayList<>();
    BigDecimal totalAcumulado = BigDecimal.ZERO;

    if (request.getItems() == null || request.getItems().isEmpty()) {
      throw new IllegalArgumentException("El carrito está vacío.");
    }

    for (CheckoutItemRequest item : request.getItems()) {
      Producto producto = productoRepository.findById(item.getProductoId())
          .orElseThrow(() -> new EntityNotFoundException(
              "Producto no encontrado con ID: " + item.getProductoId()));

      BigDecimal cantidad = BigDecimal.valueOf(item.getCantidad());

      BigDecimal precioUnit = producto.getPrecioOnline() != null
          ? producto.getPrecioOnline()
          : producto.getPrecio();

      if (precioUnit == null) {
        throw new IllegalStateException(
            "El producto " + producto.getNombre() + " no tiene precio configurado.");
      }

      BigDecimal subtotalLinea = precioUnit.multiply(cantidad);

      VentaDetalle detalle = new VentaDetalle();
      detalle.setProducto(producto);
      detalle.setCantidad(cantidad);
      detalle.setPrecioUnitario(precioUnit);
      detalle.setDescuentoUnitario(BigDecimal.ZERO);
      detalle.setSubtotal(subtotalLinea);
      detalle.setVenta(venta);

      detallesList.add(detalle);
      totalAcumulado = totalAcumulado.add(subtotalLinea);
    }

    BigDecimal totalVenta = totalAcumulado.setScale(2, RoundingMode.HALF_UP);
    BigDecimal subtotalVenta = totalVenta.divide(UNO_MAS_IGV, 2, RoundingMode.HALF_UP);
    BigDecimal igvVenta = totalVenta.subtract(subtotalVenta);

    venta.setTotal(totalVenta);
    venta.setSubtotal(subtotalVenta);
    venta.setIgv(igvVenta);
    venta.setDetalles(detallesList);

    return ventaRepository.save(venta);
  }

      public List<VentaDto> listarVentasDeClientePorDni(String dniCliente) {
        if (dniCliente == null || dniCliente.isBlank()) {
            return List.of();
        }

        return ventaRepository
                .findByClienteNumeroDocumentoOrderByFechaVentaDesc(dniCliente)
                .stream()
                .map(ventaMapper::toDto)
                .collect(Collectors.toList());
    }

  @Transactional(readOnly = true)
  public VentaDto obtenerVentaPorId(Long id) {
    Venta venta = ventaRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con ID: " + id));
    return ventaMapper.toDto(venta);
  }

  @Transactional(readOnly = true)
  public List<VentaDto> listarTodasLasVentas() {
    return ventaRepository.findAll()
        .stream()
        .map(ventaMapper::toDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public VentaDto anularVenta(Long id) {
    Venta venta = ventaRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con ID: " + id));
    if (venta.getEstado() == EstadoVenta.ANULADA) {
      throw new IllegalStateException("La venta con ID: " + id + " ya se encuentra anulada.");
    }
    venta.setEstado(EstadoVenta.ANULADA);
    for (VentaDetalle detalle : venta.getDetalles()) {
      Producto producto = detalle.getProducto();
      BigDecimal cantidadDevuelta = detalle.getCantidad();
      producto.setStockActual(producto.getStockActual().add(cantidadDevuelta));
      productoRepository.save(producto);
    }
    Venta ventaAnulada = ventaRepository.save(venta);
    return ventaMapper.toDto(ventaAnulada);
  }

  @Transactional(readOnly = true)
  public String generarSiguienteNumero(String tipoComprobante) {
    String ultimoNumero = ventaRepository.obtenerUltimoNumeroPorTipo(tipoComprobante);
    if (ultimoNumero == null) {
      return "00000001";
    }

    try {
      int numeroActual = Integer.parseInt(ultimoNumero);
      int siguienteNumero = numeroActual + 1;
      return String.format("%08d", siguienteNumero);
    } catch (NumberFormatException e) {
      return "00000001";
    }
  }
}
