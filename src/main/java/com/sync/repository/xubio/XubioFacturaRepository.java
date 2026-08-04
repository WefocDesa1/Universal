package com.sync.repository.xubio;

import com.sync.entity.xubio.XubioFacturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface XubioFacturaRepository extends JpaRepository<XubioFacturaEntity, Long> {
    Optional<XubioFacturaEntity> findByXubioId(String xubioId);
    boolean existsByXubioId(String xubioId);
    List<XubioFacturaEntity> findByFechaBetween(LocalDate desde, LocalDate hasta);
    List<XubioFacturaEntity> findByClienteXubioId(String clienteXubioId);
}
