package com.sync.dto.xubio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacturaDTO {

    @JsonProperty("InvoiceID")
    private String invoiceId;

    @JsonProperty("InvoiceNumber")
    private String numero;

    @JsonProperty("InvoiceType")
    private String tipoComprobante;

    @JsonProperty("Date")
    private LocalDate fecha;

    @JsonProperty("DueDate")
    private LocalDate fechaVencimiento;

    @JsonProperty("CustomerID")
    private String clienteId;

    @JsonProperty("SubTotal")
    private BigDecimal subtotal;

    @JsonProperty("TaxAmount")
    private BigDecimal iva;

    @JsonProperty("Total")
    private BigDecimal total;

    @JsonProperty("Status")
    private String estado;
}
