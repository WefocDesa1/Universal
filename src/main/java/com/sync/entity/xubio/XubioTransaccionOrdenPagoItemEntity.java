package com.sync.entity.xubio;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "xubio_transaccion_orden_pago_items")
public class XubioTransaccionOrdenPagoItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaccion_id", nullable = false)
    private XubioTransaccionEntity transaccion;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "cuenta_tipo")
    private String cuentaTipo;

    @Column(name = "cuenta_id")
    private Long cuentaId;

    @Column(name = "moneda_id")
    private Long monedaId;

    @Column(name = "cotizacion_mon_transaccion", precision = 18, scale = 6)
    private BigDecimal cotizacionMonTransaccion;

    @Column(name = "importe_mon_principal", precision = 18, scale = 2)
    private BigDecimal importeMonPrincipal;

    @Column(name = "importe_mon_transaccion", precision = 18, scale = 2)
    private BigDecimal importeMonTransaccion;

    @Column(name = "cheque_terceros")
    private String chequeTerceros;

    @Column(name = "cheque_propios")
    private String chequePropios;

    @Column(name = "fecha_vto")
    private LocalDate fechaVto;

    @Column(name = "banco_id")
    private Long bancoId;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

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
