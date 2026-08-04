package com.sync.repository.magiis;

import com.sync.entity.magiis.MagiisCtaCteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MagiisCtaCteRepository extends JpaRepository<MagiisCtaCteEntity, Long> {
    Optional<MagiisCtaCteEntity> findByCheckingAccountId(Long checkingAccountId);
    boolean existsByCheckingAccountId(Long checkingAccountId);
}
