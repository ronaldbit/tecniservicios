package com.example.simplemvc.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteSesion {
    private Long idCliente;
    private String nombreCompleto;
    private String email;
    private String dni;
}
