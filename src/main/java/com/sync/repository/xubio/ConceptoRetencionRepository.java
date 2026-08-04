package com.sync.repository.xubio;

import com.sync.entity.xubio.ConceptoRetencionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConceptoRetencionRepository extends JpaRepository<ConceptoRetencionEntity, Long> {
    Optional<ConceptoRetencionEntity> findByXubioId(Long xubioId);
}
