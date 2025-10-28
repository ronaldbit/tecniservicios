package com.example.simplemvc.repository;

import com.example.simplemvc.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {}
