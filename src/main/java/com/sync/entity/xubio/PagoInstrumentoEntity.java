package com.sync.entity.xubio;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Ítem del array "transaccionInstrumentoDePago" de un pago a proveedor
 * (medio con el que se pagó: cuenta bancaria, cheque, etc).
 */
@Data
@Entity
@Table(name = "xubio_pago_instrumentos")
public class PagoInstrumentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pago_id", nullable = false)
    private PagoEntity pago;

    @Column(name = "transaccion_ip_id")
    private Long transaccionIpId;

    @Column(name = "tipo_cuenta")
    private Integer tipoCuenta;

    @Column(name = "cuenta_id")
    private Long cuentaId;

    @Column(name = "moneda_id")
    private Long monedaId;

    @Column(name = "cotizacion", precision = 18, scale = 6)
    private BigDecimal cotizacion;

    @Column(name = "importe", precision = 18, scale = 2)
    private BigDecimal importe;

    @Column(name = "cheque_terceros")
    private Integer chequeTerceros;

    @Column(name = "cheque_propio")
    private String chequePropio;

    @Column(name = "vencimiento_cheque")
    private LocalDate vencimientoCheque;

    @Column(name = "banco_id")
    private Long bancoId;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

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
