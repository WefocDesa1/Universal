package com.sync.repository.xubio;

import com.sync.entity.xubio.XubioClienteEntity;
import com.sync.entity.xubio.XubioProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface XubioProveedorRepository extends JpaRepository<XubioProveedorEntity, Long> {
    Optional<XubioProveedorEntity> findByXubioId(String xubioId);
    boolean existsByXubioId(String xubioId);
}
