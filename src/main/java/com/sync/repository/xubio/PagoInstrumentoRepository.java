package com.sync.repository.xubio;

import com.sync.entity.xubio.PagoInstrumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoInstrumentoRepository extends JpaRepository<PagoInstrumentoEntity, Long> {
}
