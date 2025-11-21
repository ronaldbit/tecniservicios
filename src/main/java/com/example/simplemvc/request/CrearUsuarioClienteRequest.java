package com.example.simplemvc.request;

import lombok.Data;

@Data
public class CrearUsuarioClienteRequest {
    private Long tipoDocumentoId;
    private String numeroDocumento;
    private String tipoPersona;
    private String nombres;
    private String apellidos;
    private String razonSocial;
    private String email;
    private String telefono;
    private String direccion;
}
