package com.example.simplemvc.request;

import lombok.Data;

@Data
public class ActualizarInventarioRequest {
    private long idProducto;
    private String modo;  //AUMENTAR o QUITAR
    private double cantidad; 
}
