package com.example.simplemvc.request;

import java.util.List;
import lombok.Data;

@Data
public class CheckoutRequest {
  private String nombres;
  private String apellidos;
  private String empresa;
  private String direccion;
  private String direccion2;
  private String ciudad;
  private String provincia;
  private String codigoPostal;
  private String email;
  private String telefono;
  private String comentario;
  private boolean crearCuenta;
  private String documento;
  private List<CheckoutItemRequest> items;
}
