package com.example.simplemvc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "pedidos_proveedor_detalle")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProveedorDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private PedidoProveedor pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal cantidad;

    @Column(name = "precio_costo", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioCosto; 

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal; 
}