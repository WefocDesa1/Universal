package com.sync.repository.xubio;

import com.sync.entity.xubio.PagoRetencionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRetencionRepository extends JpaRepository<PagoRetencionEntity, Long> {
}
