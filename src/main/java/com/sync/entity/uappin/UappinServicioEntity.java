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

    @Column(name = "`NroServicio`", nullable = false, unique = true)
    private Long nroServicio;

    @Column(name = "`FechaInicioToma`")
    private LocalDateTime fechaInicioToma;

    @Column(name = "`TomadorReserva`")
    private String tomadorReserva;

    @Column(name = "`FechaFinToma`")
    private LocalDateTime fechaFinToma;

    @Column(name = "`FormaIngreso`")
    private String formaIngreso;

    @Column(name = "`Cliente`")
    private String cliente;

    @Column(name = "`TipoDocumento`")
    private String tipoDocumento;

    @Column(name = "`DocumentoNumero`")
    private String documentoNumero;

    @Column(name = "`PasajeroNombre`")
    private String pasajeroNombre;

    @Column(name = "`Origen`", columnDefinition = "TEXT")
    private String origen;

    @Column(name = "`Destino`", columnDefinition = "TEXT")
    private String destino;

    @Column(name = "`PrecioServicio`", precision = 18, scale = 2)
    private BigDecimal precioServicio;

    @Column(name = "`PrecioAdicionales`", precision = 18, scale = 2)
    private BigDecimal precioAdicionales;

    @Column(name = "`PlusNocturno`", precision = 18, scale = 2)
    private BigDecimal plusNocturno;

    @Column(name = "`TotalServicio`", precision = 18, scale = 2)
    private BigDecimal totalServicio;

    @Column(name = "`ComisionChoferPorServicio`", precision = 18, scale = 2)
    private BigDecimal comisionChoferPorServicio;

    @Column(name = "`GastosChofer`", precision = 18, scale = 2)
    private BigDecimal gastosChofer;

    @Column(name = "`FechaServicio`")
    private LocalDate fechaServicio;

    @Column(name = "`HoraServicio`")
    private LocalTime horaServicio;

    @Column(name = "`FechaFinServicio`")
    private LocalDate fechaFinServicio;

    @Column(name = "`HoraFinServicio`")
    private LocalTime horaFinServicio;

    @Column(name = "`DuracionServicio`")
    private Integer duracionServicio;

    @Column(name = "`KmsCartografia`")
    private Integer kmsCartografia;

    @Column(name = "`CategoriaServicio`")
    private String categoriaServicio;

    @Column(name = "`AdicionalesServicio`", columnDefinition = "TEXT")
    private String adicionalesServicio;

    @Column(name = "`AtributosServicio`", columnDefinition = "TEXT")
    private String atributosServicio;

    @Column(name = "`APU`")
    private String apu;

    @Column(name = "`CodigoConductor`")
    private String codigoConductor;

    @Column(name = "`Conductor`")
    private String conductor;

    @Column(name = "`ConductorPropio`")
    private String conductorPropio;

    @Column(name = "`TipoConductor`")
    private String tipoConductor;

    @Column(name = "`Movil`")
    private String movil;

    @Column(name = "`Patente`")
    private String patente;

    @Column(name = "`TipoDePago`")
    private String tipoDePago;

    @Column(name = "`UnidadNegocio`")
    private String unidadNegocio;

    @Column(name = "`EstadoServicio`")
    private String estadoServicio;

    @Column(name = "`LiquidacionChofer`")
    private String liquidacionChofer;

    @Column(name = "`OrdenNro`")
    private Long ordenNro;

    @Column(name = "`FacturaNro`", columnDefinition = "TEXT")
    private String facturaNro;

    @Column(name = "`FechaHoraUltimaModi`")
    private LocalDate fechaHoraUltimaModi;

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
