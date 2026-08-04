package com.sync.repository.xubio;

import com.sync.entity.xubio.XubioClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface XubioClienteRepository extends JpaRepository<XubioClienteEntity, Long> {
    Optional<XubioClienteEntity> findByXubioId(String xubioId);
    boolean existsByXubioId(String xubioId);
}
