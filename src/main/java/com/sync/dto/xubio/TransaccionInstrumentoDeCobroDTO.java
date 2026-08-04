package com.sync.dto.xubio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO para los instrumentos de cobro dentro de una cobranza de cliente.
 * Corresponde al array "transaccionInstrumentoDeCobro" en el JSON de /cobranzaBean.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransaccionInstrumentoDeCobroDTO {

    @JsonProperty("transaccionICId")
    private Long transaccionICId;

    @JsonProperty("cuentaTipo")
    private Integer cuentaTipo;

    @JsonProperty("cuenta")
    private EntidadRefDTO cuenta;

    @JsonProperty("moneda")
    private EntidadRefDTO moneda;

    @JsonProperty("cotizacion")
    private BigDecimal cotizacion;

    @JsonProperty("importe")
    private BigDecimal importe;

    @JsonProperty("descripcion")
    private String descripcion;
}
