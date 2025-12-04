package com.example.simplemvc.request;

import lombok.Data;

@Data
public class CheckoutItemRequest {
  private Long productoId;
  private String nombre;   // opcional, solo informativo
  private double precio;   // viene del front, pero el backend recalcula
  private int cantidad;
}
