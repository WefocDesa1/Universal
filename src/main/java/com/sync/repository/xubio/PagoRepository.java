package com.sync.repository.xubio;

import com.sync.entity.xubio.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity, Long> {
    Optional<PagoEntity> findByXubioId(Long xubioId);
    boolean existsByXubioId(Long xubioId);
    List<PagoEntity> findByFechaBetween(LocalDate desde, LocalDate hasta);
    List<PagoEntity> findByProveedorXubioId(Long proveedorXubioId);
}
