package com.sync.repository.xubio;

import com.sync.entity.xubio.PuntoVentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PuntoVentaRepository extends JpaRepository<PuntoVentaEntity, Long> {
    Optional<PuntoVentaEntity> findByXubioId(Long xubioId);
}
