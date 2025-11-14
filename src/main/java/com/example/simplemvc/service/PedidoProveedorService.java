package com.example.simplemvc.service;

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
import com.example.simplemvc.request.CrearPedidoDetalleRequest;
import com.example.simplemvc.request.CrearPedidoProveedorRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        pedido.setTotalPedido(BigDecimal.ZERO);
        pedido.setDetalles(new ArrayList<>());
        BigDecimal totalAcumulado = BigDecimal.ZERO;
        for (CrearPedidoDetalleRequest detalleRequest : request.getDetalles()) {
            Producto producto = productoRepository.findById(detalleRequest.getIdProducto())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Producto no encontrado con ID: " + detalleRequest.getIdProducto()));
            PedidoProveedorDetalle detalle = new PedidoProveedorDetalle();
            detalle.setProducto(producto);
            detalle.setCantidad(detalleRequest.getCantidad());
            detalle.setPrecioCosto(detalleRequest.getPrecioCosto());
            BigDecimal subtotal = detalleRequest.getCantidad().multiply(detalleRequest.getPrecioCosto());
            detalle.setSubtotal(subtotal);
            totalAcumulado = totalAcumulado.add(subtotal);
            detalle.setPedido(pedido);
            pedido.getDetalles().add(detalle);
        }
        pedido.setTotalPedido(totalAcumulado);
        PedidoProveedor pedidoGuardado = pedidoProveedorRepository.save(pedido);
        servicioCorreo.enviarNotificacionNuevoPedidoProveedor(pedidoGuardado);
        return pedidoProveedorMapper.toDto(pedidoGuardado);
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
    public void confirmarPedidoPorId(Long id) {
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
            producto.setStockActual(nuevoStock);
            productoRepository.save(producto);
        });
        pedidoToUpdate.setEstado(EstadoPedido.RECIBIDO);
        pedidoProveedorRepository.save(pedidoToUpdate);
    }

    @Transactional
    public void cancelarPedidoPorId(Long id) {
        if (!pedidoProveedorRepository.existsById(id)) {
            throw new EntityNotFoundException("Pedido no encontrado con ID: " + id);
        }
        pedidoProveedorRepository.findById(id).ifPresent(pedido -> {
            pedido.setEstado(EstadoPedido.CANCELADO);
            pedidoProveedorRepository.save(pedido);
        });
    }

}