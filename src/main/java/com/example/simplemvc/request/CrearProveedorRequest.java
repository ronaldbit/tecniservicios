package com.example.simplemvc.request;

import lombok.Data;

@Data
public class CrearProveedorRequest {
    private String ruc;
    private String razonSocial;
    private String direccion;
    private String telefono;
    private String email;
}
