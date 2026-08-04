package com.sync.entity.magiis;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "magiis_viajes")
public class MagiisViajeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "travel_id_for_carrier", nullable = false, unique = true)
    private Long travelIdForCarrier;

    @Column(name = "state")
    private String state;

    @Column(name = "passenger_first_name")
    private String passengerFirstName;

    @Column(name = "passenger_last_name")
    private String passengerLastName;

    @Column(name = "driver_first_name")
    private String driverFirstName;

    @Column(name = "driver_last_name")
    private String driverLastName;

    @Column(name = "vehicle_mark")
    private String vehicleMark;

    @Column(name = "vehicle_model")
    private String vehicleModel;

    @Column(name = "vehicle_domain")
    private String vehicleDomain;

    @Column(name = "car_plate")
    private String carPlate;

    @Column(name = "origin", columnDefinition = "TEXT")
    private String origin;

    @Column(name = "destination", columnDefinition = "TEXT")
    private String destination;

    @Column(name = "creation_date")
    private OffsetDateTime creationDate;

    @Column(name = "final_price", precision = 18, scale = 2)
    private BigDecimal finalPrice;

    @Column(name = "simulate_price", precision = 18, scale = 2)
    private BigDecimal simulatePrice;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "is_programmed")
    private Boolean isProgrammed;

    @Column(name = "delivery")
    private Boolean delivery;

    @Column(name = "raw_json", columnDefinition = "TEXT")
    private String rawJson;

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
