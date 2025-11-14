package com.example.simplemvc.request;

import com.example.simplemvc.model.enums.MetodoPago;
import lombok.Data;
import java.util.List;

@Data
public class CrearVentaRequest {
    private String clienteTipoDocumento; 
    private String clienteNumeroDocumento; 
    private String clienteNombreCompleto; 
    private String clienteDireccion;   
    private String tipoComprobante; 
    private String serieComprobante;
    private String numeroComprobante;
    private MetodoPago metodoPago;    
    private List<CrearVentaDetalleRequest> detalles;
}