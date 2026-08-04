package com.sync.entity.xubio;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Ítem del array "transaccionRetencionItems" de un pago a proveedor.
 */
@Data
@Entity
@Table(name = "xubio_pago_retenciones")
public class PagoRetencionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pago_id", nullable = false)
    private PagoEntity pago;

    @Column(name = "transaccion_r_item_id")
    private Long transaccionRItemId;

    @Column(name = "tipo_retencion")
    private String tipoRetencion;

    @Column(name = "concepto_retencion_id")
    private Long conceptoRetencionId;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "numero_comprobante")
    private String numeroComprobante;

    @Column(name = "moneda_id")
    private Long monedaId;

    @Column(name = "cotizacion", precision = 18, scale = 6)
    private BigDecimal cotizacion;

    @Column(name = "importe_isar", precision = 18, scale = 2)
    private BigDecimal importeISAR;

    @Column(name = "importe_retenido", precision = 18, scale = 2)
    private BigDecimal importeRetenido;

    @Column(name = "importe_mon_ppal", precision = 18, scale = 2)
    private BigDecimal importeMonPpal;

    @Column(name = "fecha_comprobante")
    private LocalDate fechaComprobante;

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
