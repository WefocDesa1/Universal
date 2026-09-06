package com.sync.entity.uappin;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "uappin_servicios")
public class UappinServicioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nro_servicio", nullable = false, unique = true)
    private Long nroServicio;

    @Column(name = "fecha_inicio_toma")
    private LocalDateTime fechaInicioToma;

    @Column(name = "tomador_reserva")
    private String tomadorReserva;

    @Column(name = "fecha_fin_toma")
    private LocalDateTime fechaFinToma;

    @Column(name = "forma_ingreso")
    private String formaIngreso;

    @Column(name = "cliente")
    private String cliente;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "documento_numero")
    private String documentoNumero;

    @Column(name = "pasajero_nombre")
    private String pasajeroNombre;

    @Column(name = "origen", columnDefinition = "TEXT")
    private String origen;

    @Column(name = "destino", columnDefinition = "TEXT")
    private String destino;

    @Column(name = "precio_servicio", precision = 18, scale = 2)
    private BigDecimal precioServicio;

    @Column(name = "precio_adicionales", precision = 18, scale = 2)
    private BigDecimal precioAdicionales;

    @Column(name = "plus_nocturno", precision = 18, scale = 2)
    private BigDecimal plusNocturno;

    @Column(name = "total_servicio", precision = 18, scale = 2)
    private BigDecimal totalServicio;

    @Column(name = "comision_chofer_por_servicio", precision = 18, scale = 2)
    private BigDecimal comisionChoferPorServicio;

    @Column(name = "gastos_chofer", precision = 18, scale = 2)
    private BigDecimal gastosChofer;

    @Column(name = "fecha_servicio")
    private LocalDate fechaServicio;

    @Column(name = "hora_servicio")
    private LocalTime horaServicio;

    @Column(name = "fecha_fin_servicio")
    private LocalDate fechaFinServicio;

    @Column(name = "hora_fin_servicio")
    private LocalTime horaFinServicio;

    @Column(name = "duracion_servicio")
    private Integer duracionServicio;

    @Column(name = "kms_realizados")
    private Integer kmsRealizados;

    @Column(name = "categoria_servicio")
    private String categoriaServicio;

    @Column(name = "adicionales_servicio", columnDefinition = "TEXT")
    private String adicionalesServicio;

    @Column(name = "atributos_servicio", columnDefinition = "TEXT")
    private String atributosServicio;

    @Column(name = "apu")
    private String apu;

    @Column(name = "codigo_conductor")
    private String codigoConductor;

    @Column(name = "conductor")
    private String conductor;

    @Column(name = "conductor_propio")
    private String conductorPropio;

    @Column(name = "movil")
    private String movil;

    @Column(name = "patente")
    private String patente;

    @Column(name = "tipo_de_pago")
    private String tipoDePago;

    @Column(name = "unidad_negocio")
    private String unidadNegocio;

    @Column(name = "estado_servicio")
    private String estadoServicio;

    @Column(name = "liquidacion_chofer")
    private String liquidacionChofer;

    @Column(name = "orden_nro")
    private Long ordenNro;

    @Column(name = "factura_nro")
    private String facturaNro;

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
