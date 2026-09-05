package com.sync.repository.uappin;

import com.sync.entity.uappin.UappinArchivoProcesadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UappinArchivoProcesadoRepository extends JpaRepository<UappinArchivoProcesadoEntity, Long> {
    boolean existsByNombreArchivo(String nombreArchivo);
}
