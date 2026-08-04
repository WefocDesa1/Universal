package com.sync.repository.xubio.transacciones;

import com.sync.entity.xubio.XubioTransaccionProductoItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XubioTransaccionProductoItemRepository 
        extends JpaRepository<XubioTransaccionProductoItemEntity, Long> {
    List<XubioTransaccionProductoItemEntity> findByTransaccion_TransaccionId(Long transaccionId);
}
