package com.sync.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sync_execution_log")
public class SyncExecutionLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fuente: XUBIO | MAGIIS */
    @Column(name = "source", nullable = false)
    private String source;

    /** Entidad: FACTURAS | CLIENTES | PRODUCTOS | VENTAS | STOCK */
    @Column(name = "entity_type", nullable = false)
    private String entityType;

    /** Estado: SUCCESS | FAILED | PARTIAL */
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "records_fetched")
    private int recordsFetched;

    @Column(name = "records_created")
    private int recordsCreated;

    @Column(name = "records_updated")
    private int recordsUpdated;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(name = "executed_at")
    private LocalDateTime executedAt;

    @PrePersist
    protected void onCreate() {
        executedAt = LocalDateTime.now();
    }
}
