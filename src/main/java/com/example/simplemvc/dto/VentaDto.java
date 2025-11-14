package com.example.simplemvc.dto;

import com.example.simplemvc.model.enums.EstadoVenta;
import com.example.simplemvc.model.enums.MetodoPago;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class VentaDto {
    private Long idVenta;    
    private Long idUsuarioVendedor;
    private String nombreVendedor; 
    private String clienteTipoDocumento; 
    private String clienteNumeroDocumento; 
    private String clienteNombreCompleto; 
    private String clienteDireccion;
    private Timestamp fechaVenta;
    private EstadoVenta estado;
    private String tipoComprobante; 
    private String serieComprobante;
    private String numeroComprobante;
    private MetodoPago metodoPago;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;
    private List<VentaDetalleDto> detalles;
}