package com.sync.dto.magiis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MagiisCtaCteDTO {

    @JsonProperty("checkingAccountId")
    private Long checkingAccountId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("passengerClass")
    private String passengerClass;  // COMPANY | INDIVIDUAL

    @JsonProperty("balance")
    private BigDecimal balance;

    @JsonProperty("limit")
    private BigDecimal limit;
}
