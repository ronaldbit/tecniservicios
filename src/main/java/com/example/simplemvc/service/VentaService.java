package com.example.simplemvc.service;

import com.example.simplemvc.dto.VentaDto;
import com.example.simplemvc.model.*;
import com.example.simplemvc.model.enums.EstadoVenta;
import com.example.simplemvc.repository.ProductoRepository;
import com.example.simplemvc.repository.VentaRepository;
import com.example.simplemvc.request.CrearVentaDetalleRequest;
import com.example.simplemvc.request.CrearVentaRequest;
import com.example.simplemvc.shared.Exeption.StockInsuficienteException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final VentaMapper ventaMapper;

    private final GetActualUsuarioService getActualUsuarioService;

    private static final BigDecimal TASA_IGV = new BigDecimal("0.18");
    private static final BigDecimal UNO_MAS_IGV = new BigDecimal("1.18");

    @Transactional
    public VentaDto crearVenta(CrearVentaRequest request) {

        Usuario vendedor = getActualUsuarioService.get();
        if (vendedor == null) {
            throw new IllegalStateException("No se pudo identificar al vendedor. El usuario no está autenticado.");
        }
        if (request.getNumeroComprobante() == null) {
            int numero = (int) (Math.random() * 900000) + 100000; 
            request.setNumeroComprobante(String.valueOf(numero));
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
        Venta ventaGuardada = ventaRepository.save(venta);
        return ventaMapper.toDto(ventaGuardada);
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
}