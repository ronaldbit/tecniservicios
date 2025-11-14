package com.example.simplemvc.repository;

import com.example.simplemvc.model.PedidoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoProveedorRepository extends JpaRepository<PedidoProveedor, Long> {
}