package com.sync.repository.xubio;

import com.sync.entity.xubio.CobranzaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CobranzaRepository extends JpaRepository<CobranzaEntity, Long> {
    Optional<CobranzaEntity> findByXubioId(Long xubioId);
    boolean existsByXubioId(Long xubioId);
    List<CobranzaEntity> findByFechaBetween(LocalDate desde, LocalDate hasta);
    List<CobranzaEntity> findByClienteXubioId(Long clienteXubioId);
}
