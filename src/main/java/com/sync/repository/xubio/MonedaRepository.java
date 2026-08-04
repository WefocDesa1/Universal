package com.sync.repository.xubio;

import com.sync.entity.xubio.MonedaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonedaRepository extends JpaRepository<MonedaEntity, Long> {
    Optional<MonedaEntity> findByXubioId(Long xubioId);
}
