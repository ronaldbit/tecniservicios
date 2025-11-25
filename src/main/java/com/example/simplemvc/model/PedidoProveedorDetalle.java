package com.example.simplemvc.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

  @Column(name = "precio_unitario", precision = 12, scale = 2)
  private BigDecimal precioUnitario;

  @Column(name = "recibido_cantidad", precision = 12, scale = 2)
  private BigDecimal recibidoCantidad;

  @Column(name = "recibido", nullable = false)
  private boolean recibido;
}