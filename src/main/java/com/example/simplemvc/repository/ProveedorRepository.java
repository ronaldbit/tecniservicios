package com.example.simplemvc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simplemvc.model.Proveedor;

public interface ProveedorRepository extends JpaRepository<com.example.simplemvc.model.Proveedor, Long> {
    Optional<Proveedor> findByRuc(String ruc);
}
