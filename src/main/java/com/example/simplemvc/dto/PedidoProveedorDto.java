package com.example.simplemvc.dto;

import com.example.simplemvc.model.enums.EstadoPedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProveedorDto {
    private Long id;    
    private Long idProveedor;    
    private String razonSocialProveedor;
    private LocalDate fechaEmision;    
    private LocalDate fechaEntregaEsperada;    
    private String notas;    
    private EstadoPedido estado;    
    private String nombreArchivoFactura;
    private BigDecimal costoCotizacion;    
    private Timestamp createdAt;
    private List<PedidoProveedorDetalleDto> detalles;
}