package com.example.simplemvc.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.simplemvc.dto.PedidoProveedorDto;
import com.example.simplemvc.model.PedidoProveedor;
import com.example.simplemvc.model.PedidoProveedorDetalle;
import com.example.simplemvc.model.PedidoProveedorMapper;
import com.example.simplemvc.model.Producto;
import com.example.simplemvc.model.Proveedor;
import com.example.simplemvc.model.enums.EstadoPedido;
import com.example.simplemvc.repository.PedidoProveedorRepository;
import com.example.simplemvc.repository.ProductoRepository;
import com.example.simplemvc.repository.ProveedorRepository;
import com.example.simplemvc.request.CotizacionRequest;
import com.example.simplemvc.request.CrearPedidoDetalleRequest;
import com.example.simplemvc.request.CrearPedidoProveedorRequest;
import com.example.simplemvc.request.DetalleCotizacionItem;
import com.example.simplemvc.request.DetalleReciboRequest;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoProveedorService {
  private final PedidoProveedorRepository pedidoProveedorRepository;
  private final ProveedorRepository proveedorRepository;
  private final ProductoRepository productoRepository;
  private final PedidoProveedorMapper pedidoProveedorMapper;
  private final ServicioCorreo servicioCorreo;

  @Transactional
  public PedidoProveedorDto crearPedido(CrearPedidoProveedorRequest request) {
    Proveedor proveedor = proveedorRepository.findById(request.getIdProveedor())
        .orElseThrow(() -> new EntityNotFoundException(
            "Proveedor no encontrado con ID: " + request.getIdProveedor()));
    PedidoProveedor pedido = new PedidoProveedor();
    pedido.setProveedor(proveedor);
    pedido.setFechaEmision(request.getFechaEmision());
    pedido.setFechaEntregaEsperada(request.getFechaEntregaEsperada());
    pedido.setNotas(request.getNotas());
    pedido.setCostoCotizacion(null);
    pedido.setDetalles(new ArrayList<>());
    for (CrearPedidoDetalleRequest detalleRequest : request.getDetalles()) {
      Producto producto = productoRepository.findById(detalleRequest.getIdProducto())
          .orElseThrow(() -> new EntityNotFoundException(
              "Producto no encontrado con ID: " + detalleRequest.getIdProducto()));
      PedidoProveedorDetalle detalle = new PedidoProveedorDetalle();
      detalle.setProducto(producto);
      detalle.setCantidad(detalleRequest.getCantidad());
      detalle.setRecibido(false);
      detalle.setPedido(pedido);
      pedido.getDetalles().add(detalle);
    }
    request.setCostoCotizacion(request.getCostoCotizacion());
    pedido.setEstado(EstadoPedido.BORRADOR);
    PedidoProveedor pedidoGuardado = pedidoProveedorRepository.save(pedido);
    servicioCorreo.enviarNotificacionNuevoPedidoProveedor(pedidoGuardado);
    return pedidoProveedorMapper.toDto(pedidoGuardado);
  }

  @Transactional
  public void EstablecerCotizacionPedido(Long id, CotizacionRequest cotizacionRequest) {
    PedidoProveedor pedido = pedidoProveedorRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado: " + id));
    pedido.setCostoCotizacion(cotizacionRequest.getCostoCotizacion());
    pedido.setFechaEntregaEsperada(cotizacionRequest.getFechaEntregaEsperada());
    pedido.setEstado(EstadoPedido.COTIZADO);
    if (cotizacionRequest.getDetalles() != null) {
      for (DetalleCotizacionItem item : cotizacionRequest.getDetalles()) {
        pedido.getDetalles().stream()
            .filter(d -> d.getId().equals(item.getIdDetalle()))
            .findFirst()
            .ifPresent(detalle -> {
              detalle.setPrecioUnitario(item.getPrecioUnitario());
            });
      }
    }
    pedidoProveedorRepository.save(pedido);
    servicioCorreo.enviarNotificacionNuevoPedidoProveedor(pedido);
  }

  @Transactional
  public PedidoProveedorDto obtenerPedidoPorId(Long id) {
    PedidoProveedor pedido = pedidoProveedorRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(
            "Pedido no encontrado con ID: " + id));
    return pedidoProveedorMapper.toDto(pedido);
  }

  @Transactional
  public List<PedidoProveedorDto> obtenerTodosLosPedidos() {
    List<PedidoProveedor> pedidos = pedidoProveedorRepository.findAll();
    List<PedidoProveedorDto> pedidosDto = new ArrayList<>();
    for (PedidoProveedor pedido : pedidos) {
      pedidosDto.add(pedidoProveedorMapper.toDto(pedido));
    }
    return pedidosDto;
  }

  @Transactional
  public void confirmarPedidoPorId(Long id, String nombreArchivoFactura) {
    if (!pedidoProveedorRepository.existsById(id)) {
      throw new EntityNotFoundException("Pedido no encontrado con ID: " + id);
    }
    Optional<PedidoProveedor> pedido = pedidoProveedorRepository.findById(id);
    PedidoProveedor pedidoToUpdate = pedido.get();

    pedidoToUpdate.getDetalles().forEach(detalle -> {
      Producto producto = productoRepository.findById(detalle.getProducto().getIdProducto())
          .orElseThrow(() -> new EntityNotFoundException(
              "Producto no encontrado con ID: " + detalle.getProducto().getIdProducto()));
      BigDecimal nuevoStock = producto.getStockActual().add(detalle.getCantidad());
      detalle.setRecibido(true);
      pedidoToUpdate.setNombreArchivoFactura(nombreArchivoFactura);
      producto.setStockActual(nuevoStock);
      detalle.setPedido(pedidoToUpdate);
      productoRepository.save(producto);
    });
    pedidoToUpdate.setEstado(EstadoPedido.RECIBIDO);
    pedidoProveedorRepository.save(pedidoToUpdate);
  }

  @Transactional
  public void confirmarAprobacionporId(Long id) {
    PedidoProveedor pedido = pedidoProveedorRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + id));
    pedido.setEstado(EstadoPedido.PENDIENTE);
    PedidoProveedor pedidoGuardado = pedidoProveedorRepository.save(pedido);
    servicioCorreo.enviarConfirmacionPedido(pedidoGuardado);
  }

  @Transactional
  public void cancelarPedidoPorId(Long id) {
    if (!pedidoProveedorRepository.existsById(id)) {
      throw new EntityNotFoundException("Pedido no encontrado con ID: " + id);
    }
    pedidoProveedorRepository.findById(id).ifPresent(pedido -> {
      pedido.setEstado(EstadoPedido.CANCELADO);
      pedido.getDetalles().forEach(detalle -> detalle.setRecibido(false));
      pedidoProveedorRepository.save(pedido);
    });
  }

  @Transactional
  public void cancelarCotizacion(Long id) {
    PedidoProveedor pedido = pedidoProveedorRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(
            "Pedido no encontrado con ID: " + id));
    pedido.setCostoCotizacion(null);
    pedido.setEstado(EstadoPedido.COTIZACION_CANCELADA);
    pedidoProveedorRepository.save(pedido);
  }

  @Transactional
  public void reciboParcialPedidoPorId(Long id, List<DetalleReciboRequest> request) {
    PedidoProveedor pedido = pedidoProveedorRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + id));

    for (DetalleReciboRequest dto : request) {
      pedido.getDetalles().stream()
          .filter(det -> det.getId().equals(dto.getIdDetalle()))
          .findFirst()
          .ifPresent(detalle -> {
            BigDecimal cantidadLlegadaAhora = dto.getRecibidoCantidad();
            if (cantidadLlegadaAhora != null && cantidadLlegadaAhora.compareTo(BigDecimal.ZERO) > 0) {
              BigDecimal recibidoAntes = detalle.getRecibidoCantidad() != null ? detalle.getRecibidoCantidad()
                  : BigDecimal.ZERO;
              BigDecimal nuevoTotal = recibidoAntes.add(cantidadLlegadaAhora);
              if (nuevoTotal.compareTo(detalle.getCantidad()) > 0) {
                throw new IllegalStateException(
                    "Error: La cantidad total recibida supera lo solicitado para el producto "
                        + detalle.getProducto().getNombre());
              }
              detalle.setRecibidoCantidad(nuevoTotal);
              if (nuevoTotal.compareTo(detalle.getCantidad()) >= 0) {
                detalle.setRecibido(true);
              }
              Producto producto = detalle.getProducto();
              producto.setStockActual(producto.getStockActual().add(cantidadLlegadaAhora));
              productoRepository.save(producto);
            }
          });
    }
    boolean todosCompletos = pedido.getDetalles().stream()
        .allMatch(d -> d.isRecibido());
    boolean algunRecibido = pedido.getDetalles().stream()
        .anyMatch(d -> d.getRecibidoCantidad() != null && d.getRecibidoCantidad().compareTo(BigDecimal.ZERO) > 0);

    if (todosCompletos) {
      pedido.setEstado(EstadoPedido.PENDIENTE_SUBIR_VOUCHER);
    } else if (algunRecibido) {
      pedido.setEstado(EstadoPedido.PARTIALMENTE_RECIBIDO);
    } else {
      pedido.setEstado(EstadoPedido.PENDIENTE);
    }

    pedidoProveedorRepository.save(pedido);
  }

}