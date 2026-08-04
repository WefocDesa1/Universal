package com.sync.repository.xubio;

import com.sync.entity.xubio.CobranzaRetencionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CobranzaRetencionRepository extends JpaRepository<CobranzaRetencionEntity, Long> {
}
