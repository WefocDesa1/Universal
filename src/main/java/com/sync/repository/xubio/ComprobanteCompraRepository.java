package com.sync.repository.xubio;

import com.sync.entity.xubio.ComprobanteCompraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComprobanteCompraRepository extends JpaRepository<ComprobanteCompraEntity, Long> {
    Optional<ComprobanteCompraEntity> findByXubioId(Long xubioId);
    boolean existsByXubioId(Long xubioId);
    List<ComprobanteCompraEntity> findByFechaBetween(LocalDate desde, LocalDate hasta);
    List<ComprobanteCompraEntity> findByProveedorXubioId(Long proveedorXubioId);
    List<ComprobanteCompraEntity> findByEstado(String estado);
}
