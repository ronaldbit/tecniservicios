package com.example.simplemvc.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearPedidoProveedorRequest {
    private Long idProveedor;
    private LocalDate fechaEmision;    
    private LocalDate fechaEntregaEsperada;    
    private String notas;
    private List<CrearPedidoDetalleRequest> detalles;
}