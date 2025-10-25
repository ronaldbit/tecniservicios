package com.example.simplemvc.model;

import java.time.LocalDateTime;

public class Usuario {
    private Long idUsuario;
    private Long idPersona;
    private Long idSucursal;
    private String nombreUsuario;
    private String hashPass;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long idRol;

    public Usuario(Long idUsuario, Long idPersona, Long idSucursal, String nombreUsuario, String hashPass, Boolean activo,
                   LocalDateTime createdAt, LocalDateTime updatedAt, Long idRol) {
        this.idUsuario = idUsuario;
        this.idPersona = idPersona;
        this.idSucursal = idSucursal;
        this.nombreUsuario = nombreUsuario;
        this.hashPass = hashPass;
        this.activo = activo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.idRol = idRol;
    }

    public Usuario() {}

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public Long getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Long idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getHashPass() {
        return hashPass;
    }

    public void setHashPass(String hashPass) {
        this.hashPass = hashPass;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }
}
