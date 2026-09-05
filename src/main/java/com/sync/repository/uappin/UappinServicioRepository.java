package com.sync.repository.uappin;

import com.sync.entity.uappin.UappinServicioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UappinServicioRepository extends JpaRepository<UappinServicioEntity, Long> {
    Optional<UappinServicioEntity> findByNroServicio(Long nroServicio);
}
