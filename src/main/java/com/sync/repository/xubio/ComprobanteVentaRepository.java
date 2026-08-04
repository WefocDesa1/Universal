package com.sync.repository.xubio;

import com.sync.entity.xubio.ComprobanteVentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComprobanteVentaRepository extends JpaRepository<ComprobanteVentaEntity, Long> {
    Optional<ComprobanteVentaEntity> findByXubioId(Long xubioId);
    boolean existsByXubioId(Long xubioId);
    List<ComprobanteVentaEntity> findByFechaBetween(LocalDate desde, LocalDate hasta);
    List<ComprobanteVentaEntity> findByClienteXubioId(Long clienteXubioId);
    List<ComprobanteVentaEntity> findByEstado(String estado);
}
