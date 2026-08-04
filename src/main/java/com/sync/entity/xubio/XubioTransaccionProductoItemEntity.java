package com.sync.entity.xubio;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "xubio_transaccion_producto_items")
public class XubioTransaccionProductoItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaccion_id", nullable = false)
    private XubioTransaccionEntity transaccion;

    @Column(name = "transaccion_cv_item_id")
    private Long transaccionCVItemId;

    @Column(name = "producto_id")
    private Long productoId;

    @Column(name = "centro_de_costo_id")
    private Long centroDeCostoId;

    @Column(name = "deposito_id")
    private Long depositoId;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "cantidad", precision = 18, scale = 6)
    private BigDecimal cantidad;

    @Column(name = "precio", precision = 18, scale = 6)
    private BigDecimal precio;

    @Column(name = "precio_con_iva_incluido", precision = 18, scale = 6)
    private BigDecimal precioConIvaIncluido;

    @Column(name = "iva", precision = 18, scale = 2)
    private BigDecimal iva;

    @Column(name = "importe", precision = 18, scale = 2)
    private BigDecimal importe;

    @Column(name = "total", precision = 18, scale = 2)
    private BigDecimal total;

    @Column(name = "monto_exento", precision = 18, scale = 2)
    private BigDecimal montoExento;

    @Column(name = "porcentaje_descuento", precision = 18, scale = 6)
    private BigDecimal porcentajeDescuento;

    @Column(name = "raw_json", columnDefinition = "TEXT")
    private String rawJson;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
