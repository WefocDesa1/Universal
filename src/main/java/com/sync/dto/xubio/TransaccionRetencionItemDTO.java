package com.sync.dto.xubio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransaccionRetencionItemDTO {

    @JsonProperty("transaccionRItemId")
    private Long transaccionRItemId;

    @JsonProperty("tipoRetencion")
    private String tipoRetencion;

    @JsonProperty("conceptoRetencion")
    private EntidadRefDTO conceptoRetencion;

    @JsonProperty("descripcion")
    private String descripcion;

    @JsonProperty("numeroComprobante")
    private String numeroComprobante;

    @JsonProperty("moneda")
    private EntidadRefDTO moneda;

    @JsonProperty("cotizacion")
    private BigDecimal cotizacion;

    @JsonProperty("importeISAR")
    private BigDecimal importeISAR;

    @JsonProperty("importeRetenido")
    private BigDecimal importeRetenido;

    @JsonProperty("importeMonPpal")
    private BigDecimal importeMonPpal;

    @JsonProperty("fechaComprobante")
    private OffsetDateTime fechaComprobante;

    @JsonProperty("transaccionId")
    private Long transaccionId;
}
