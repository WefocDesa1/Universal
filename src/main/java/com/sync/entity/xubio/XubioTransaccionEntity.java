package com.sync.entity.xubio;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad JPA para transacciones contables de Xubio.
 * Representa compras, ventas, órdenes de pago, etc.
 */
@Data
@Entity
@Table(name = "xubio_transacciones")
public class XubioTransaccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaccion_id", nullable = false, unique = true)
    private Long transaccionId;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "tipo")
    private Integer tipo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "fecha_fiscal")
    private LocalDate fechaFiscal;

    @Column(name = "fecha_comprobante")
    private LocalDate fechaComprobante;

    @Column(name = "fecha_vto")
    private LocalDate fechaVto;

    @Column(name = "numero_documento")
    private String numeroDocumento;

    @Column(name = "punto_venta")
    private String puntoVenta;

    @Column(name = "comprobante")
    private Integer comprobante;

    @Column(name = "condicion_de_pago")
    private Integer condicionDePago;

    // Referencias a otras entidades (IDs)
    @Column(name = "circuito_contable_id")
    private Long circuitoContableId;

    @Column(name = "cuenta_bancaria_id")
    private Long cuentaBancariaId;

    @Column(name = "deposito_id")
    private Long depositoId;

    @Column(name = "lista_de_precio_id")
    private Long listaDePrecioId;

    @Column(name = "proveedor_id")
    private Long proveedorId;

    @Column(name = "provincia_id")
    private Long provinciaId;

    // Montos
    @Column(name = "cotizacion", precision = 18, scale = 6)
    private BigDecimal cotizacion;

    @Column(name = "importe_mon_principal", precision = 18, scale = 2)
    private BigDecimal importeMonPrincipal;

    @Column(name = "importe_total", precision = 18, scale = 2)
    private BigDecimal importeTotal;

    @Column(name = "importe_impuestos", precision = 18, scale = 2)
    private BigDecimal importeImpuestos;

    @Column(name = "importe_gravado", precision = 18, scale = 2)
    private BigDecimal importeGravado;

    @Column(name = "cotizacion_lista_de_precio", precision = 18, scale = 6)
    private BigDecimal cotizacionListaDePrecio;

    @Column(name = "cbu_informada")
    private Boolean cbuinformada;

    @Column(name = "raw_json", columnDefinition = "TEXT")
    private String rawJson;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones OneToMany
    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<XubioTransaccionProductoItemEntity> productoItems;

    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<XubioTransaccionPercepcionItemEntity> percepcionItems;

    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<XubioTransaccionOrdenPagoItemEntity> ordenPagoItems;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
