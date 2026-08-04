package com.sync.dto.magiis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MagiisViajeDTO {

    @JsonProperty("travelIdForCarrier")
    private Long travelIdForCarrier;

    @JsonProperty("state")
    private String state;

    @JsonProperty("passengerFirstName")
    private String passengerFirstName;

    @JsonProperty("passengerLastName")
    private String passengerLastName;

    @JsonProperty("driverFirstName")
    private String driverFirstName;

    @JsonProperty("driverLastName")
    private String driverLastName;

    @JsonProperty("vehicleMark")
    private String vehicleMark;

    @JsonProperty("vehicleModel")
    private String vehicleModel;

    @JsonProperty("vehicleDomain")
    private String vehicleDomain;

    @JsonProperty("carPlate")
    private String carPlate;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("creationDate")
    private OffsetDateTime creationDate;

    @JsonProperty("finalPrice")
    private BigDecimal finalPrice;

    @JsonProperty("simulatePrice")
    private BigDecimal simulatePrice;

    @JsonProperty("isProgrammed")
    private Boolean isProgrammed;

    @JsonProperty("paymentMethod")
    private String paymentMethod;

    @JsonProperty("serviceType")
    private String serviceType;

    @JsonProperty("avoidTolls")
    private Boolean avoidTolls;

    @JsonProperty("avoidHighways")
    private Boolean avoidHighways;

    @JsonProperty("delivery")
    private Boolean delivery;
}
