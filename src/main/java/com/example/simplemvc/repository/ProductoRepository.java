package com.example.simplemvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simplemvc.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
        
}
