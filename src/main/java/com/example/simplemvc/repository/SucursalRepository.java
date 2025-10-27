package com.example.simplemvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simplemvc.model.Sucursal;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
}
