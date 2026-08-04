package com.sync.entity.xubio;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "xubio_facturas")
public class XubioFacturaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "xubio_id", nullable = false, unique = true)
    private String xubioId;

    @Column(name = "numero")
    private String numero;

    @Column(name = "tipo_comprobante")
    private String tipoComprobante;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "cliente_xubio_id")
    private String clienteXubioId;

    @Column(name = "subtotal", precision = 18, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "iva", precision = 18, scale = 2)
    private BigDecimal iva;

    @Column(name = "total", precision = 18, scale = 2)
    private BigDecimal total;

    @Column(name = "estado")
    private String estado;

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
