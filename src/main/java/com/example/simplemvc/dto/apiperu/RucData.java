package com.example.simplemvc.dto.apiperu;

public record RucData(
    String ruc,
    String nombre_o_razon_social,
    String direccion,
    String direccion_completa,
    String estado,
    String condicion,
    String departamento,
    String provincia,
    String distrito,
    String ubigeo_sunat) {}
