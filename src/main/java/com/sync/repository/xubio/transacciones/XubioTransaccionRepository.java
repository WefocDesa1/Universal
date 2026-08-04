package com.sync.repository.xubio.transacciones;

import com.sync.entity.xubio.XubioTransaccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface XubioTransaccionRepository extends JpaRepository<XubioTransaccionEntity, Long> {
    Optional<XubioTransaccionEntity> findByTransaccionId(Long transaccionId);
    boolean existsByTransaccionId(Long transaccionId);
    List<XubioTransaccionEntity> findByFechaBetween(LocalDate desde, LocalDate hasta);
    List<XubioTransaccionEntity> findByTipo(Integer tipo);
}
