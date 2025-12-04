package com.example.simplemvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simplemvc.model.PedidoProveedor;
import com.example.simplemvc.model.enums.EstadoPedido;

public interface PedidoProveedorRepository extends JpaRepository<PedidoProveedor, Long> {
  long countByEstado(EstadoPedido estado);
}