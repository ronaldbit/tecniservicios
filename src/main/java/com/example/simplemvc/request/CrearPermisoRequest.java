package com.example.simplemvc.request;

import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class CrearPermisoRequest {
    private String path;
    private Long rolId;    
}
