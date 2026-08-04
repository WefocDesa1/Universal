package com.sync.repository.xubio;

import com.sync.entity.xubio.CobranzaInstrumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CobranzaInstrumentoRepository extends JpaRepository<CobranzaInstrumentoEntity, Long> {
}
