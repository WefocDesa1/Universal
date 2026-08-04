package com.sync.repository.xubio;

import com.sync.entity.xubio.DepositoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepositoRepository extends JpaRepository<DepositoEntity, Long> {
    Optional<DepositoEntity> findByXubioId(Long xubioId);
}
