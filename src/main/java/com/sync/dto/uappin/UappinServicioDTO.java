package com.sync.dto.uappin;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Representa una fila del reporte de servicios de Uappin (hoja "Reporte").
 * Mapea 1:1 con las columnas del excel, en el mismo orden.
 */
@Data
public class UappinServicioDTO {
    private Long nroServicio;
    private LocalDateTime fechaInicioToma;
    private String tomadorReserva;
    private LocalDateTime fechaFinToma;
    private String formaIngreso;
    private String cliente;
    private String tipoDocumento;
    private String documentoNumero;
    private String pasajeroNombre;
    private String origen;
    private String destino;
    private BigDecimal precioServicio;
    private BigDecimal precioAdicionales;
    private BigDecimal plusNocturno;
    private BigDecimal totalServicio;
    private BigDecimal comisionChoferPorServicio;
    private BigDecimal gastosChofer;
    private LocalDate fechaServicio;
    private LocalTime horaServicio;
    private LocalDate fechaFinServicio;
    private LocalTime horaFinServicio;
    private Integer duracionServicio;
    private Integer kmsRealizados;
    private String categoriaServicio;
    private String adicionalesServicio;
    private String atributosServicio;
    private String apu;
    private String codigoConductor;
    private String conductor;
    private String conductorPropio;
    private String movil;
    private String patente;
    private String tipoDePago;
    private String unidadNegocio;
    private String estadoServicio;
    private String liquidacionChofer;
    private Long ordenNro;
    private String facturaNro;
}
