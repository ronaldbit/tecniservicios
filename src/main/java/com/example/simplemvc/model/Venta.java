package com.example.simplemvc.model;

import com.example.simplemvc.model.enums.EstadoVenta;
import com.example.simplemvc.model.enums.MetodoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "ventas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long idVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_vendedor", nullable = false)
    private Usuario vendedor;
    
    @Column(name = "cliente_tipo_documento", length = 20, nullable = false)
    private String clienteTipoDocumento; 

    @Column(name = "cliente_numero_documento", length = 20, nullable = false)
    private String clienteNumeroDocumento; 
    
    @Column(name = "cliente_nombre_completo", length = 300, nullable = false)
    private String clienteNombreCompleto; 

    @Column(name = "cliente_direccion", length = 500)
    private String clienteDireccion; 

    @CreationTimestamp
    @Column(name = "fecha_venta", nullable = false, updatable = false)
    private Timestamp fechaVenta;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_venta", length = 20, nullable = false)
    private EstadoVenta estado = EstadoVenta.COMPLETADA;

    @Column(name = "tipo_comprobante", length = 20, nullable = false)
    private String tipoComprobante; 

    @Column(name = "serie_comprobante", length = 10, nullable = false)
    private String serieComprobante;

    @Column(name = "numero_comprobante", length = 20, nullable = false)
    private String numeroComprobante;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", length = 50, nullable = false)
    private MetodoPago metodoPago;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "igv", nullable = false, precision = 12, scale = 2)
    private BigDecimal igv; 

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VentaDetalle> detalles;
}