package com.sync.repository.xubio;

import com.sync.entity.xubio.CircuitoContableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CircuitoContableRepository extends JpaRepository<CircuitoContableEntity, Long> {
    Optional<CircuitoContableEntity> findByXubioId(Long xubioId);
}
