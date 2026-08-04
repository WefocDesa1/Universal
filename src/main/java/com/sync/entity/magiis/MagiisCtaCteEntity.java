package com.sync.entity.magiis;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "magiis_cuentas_corrientes")
public class MagiisCtaCteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "checking_account_id", nullable = false, unique = true)
    private Long checkingAccountId;

    @Column(name = "name")
    private String name;

    @Column(name = "passenger_class")
    private String passengerClass; // COMPANY | INDIVIDUAL

    @Column(name = "balance", precision = 18, scale = 2)
    private BigDecimal balance;

    @Column(name = "limit_amount", precision = 18, scale = 2)
    private BigDecimal limitAmount;

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
