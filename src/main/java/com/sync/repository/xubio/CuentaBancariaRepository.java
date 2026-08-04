package com.sync.repository.xubio;

import com.sync.entity.xubio.CuentaBancariaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaBancariaRepository extends JpaRepository<CuentaBancariaEntity, Long> {
    Optional<CuentaBancariaEntity> findByXubioId(Long xubioId);
}
