package com.example.simplemvc.repository;

import com.example.simplemvc.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}