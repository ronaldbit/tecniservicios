package com.example.simplemvc.dto.apiperu;

public record DniData(
  String numero,
  String nombre_completo,
  String nombres,
  String apellido_paterno,
  String apellido_materno,
  String codigo_verificacion
) {}
