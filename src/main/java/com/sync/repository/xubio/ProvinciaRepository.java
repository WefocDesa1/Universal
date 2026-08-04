package com.sync.repository.xubio;

import com.sync.entity.xubio.ProvinciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinciaRepository extends JpaRepository<ProvinciaEntity, Long> {
    Optional<ProvinciaEntity> findByXubioId(Long xubioId);
}
