package com.sync.dto.xubio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO para Pagos / Órdenes de Pago (pagos a proveedores).
 * Endpoint: GET /pagoBean
 * Estructura basada en el JSON real de la API Xubio.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagoDTO {

    @JsonProperty("transaccionid")
    private Long transaccionId;

    @JsonProperty("circuitoContable")
    private EntidadRefDTO circuitoContable;

    @JsonProperty("proveedor")
    private EntidadRefDTO proveedor;

    @JsonProperty("fecha")
    private LocalDate fecha;

    @JsonProperty("numeroRecibo")
    private String numeroRecibo;

    @JsonProperty("cotizacion")
    private BigDecimal cotizacion;

    @JsonProperty("utilizaMonedaExtranjera")
    private Integer utilizaMonedaExtranjera;

    @JsonProperty("observacion")
    private String observacion;

    @JsonProperty("transaccionInstrumentoDePago")
    private List<TransaccionInstrumentoDePagoDTO> transaccionInstrumentoDePago;

    @JsonProperty("transaccionRetencionItems")
    private List<TransaccionRetencionItemDTO> transaccionRetencionItems;

    @JsonProperty("moneda")
    private EntidadRefDTO moneda;
}
