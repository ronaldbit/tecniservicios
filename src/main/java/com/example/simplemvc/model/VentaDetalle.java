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
@Table(name = "ventas_detalle")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaDetalle {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_venta_detalle")
  private Long idVentaDetalle;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_venta", nullable = false)
  private Venta venta;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_producto", nullable = false)
  private Producto producto;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal cantidad;

  @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
  private BigDecimal precioUnitario;

  @Column(name = "descuento_unitario", precision = 12, scale = 2)
  @Builder.Default
  private BigDecimal descuentoUnitario = BigDecimal.ZERO;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal subtotal;
}