package com.sync.entity.uappin;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "uappin_archivos_procesados")
public class UappinArchivoProcesadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_archivo", nullable = false, unique = true)
    private String nombreArchivo;

    @Column(name = "estado", nullable = false)
    private String estado; // PROCESADO | ERROR

    @Column(name = "filas_leidas")
    private Integer filasLeidas;

    @Column(name = "filas_creadas")
    private Integer filasCreadas;

    @Column(name = "filas_actualizadas")
    private Integer filasActualizadas;

    @Column(name = "error_mensaje", columnDefinition = "TEXT")
    private String errorMensaje;

    @Column(name = "procesado_en")
    private LocalDateTime procesadoEn;

    @PrePersist
    protected void onCreate() {
        procesadoEn = LocalDateTime.now();
    }
}
