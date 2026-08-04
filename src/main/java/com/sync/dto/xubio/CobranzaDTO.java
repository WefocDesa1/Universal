package com.sync.dto.xubio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * DTO para Cobranzas (cobros/ingresos de clientes).
 * Endpoint: GET /cobranzaBean
 * Estructura basada en el JSON real de la API Xubio (espejo de /pagoBean
 * pero del lado del cliente): el monto y la cuenta usada no vienen en el
 * nivel superior, están dentro de "transaccionInstrumentoDeCobro".
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CobranzaDTO {

    @JsonProperty("transaccionid")
    private Long cobranzaId;

    @JsonProperty("circuitoContable")
    private EntidadRefDTO circuitoContable;

    @JsonProperty("cliente")
    private EntidadRefDTO cliente;

    @JsonProperty("comprobanteVenta")
    private EntidadRefDTO comprobanteVenta;

    @JsonProperty("fecha")
    private OffsetDateTime fecha;

    @JsonProperty("numeroRecibo")
    private String numero;

    @JsonProperty("monedaCtaCte")
    private EntidadRefDTO monedaCtaCte;

    @JsonProperty("cotizacion")
    private BigDecimal cotizacion;

    @JsonProperty("utilizaMonedaExtranjera")
    private Integer utilizaMonedaExtranjera;

    @JsonProperty("observacion")
    private String observaciones;

    @JsonProperty("transaccionInstrumentoDeCobro")
    private List<TransaccionInstrumentoDeCobroDTO> transaccionInstrumentoDeCobro;

    @JsonProperty("transaccionRetencionItems")
    private List<TransaccionRetencionItemDTO> transaccionRetencionItems;
}
