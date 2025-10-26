package com.example.simplemvc.request;

import java.util.List;

import lombok.Data;

@Data
public class CrearUsuarioRol {
    private String nombre;
    private List<CrearPermisoRequest> permisos;
}
