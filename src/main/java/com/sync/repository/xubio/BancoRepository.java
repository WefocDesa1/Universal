package com.sync.repository.xubio;

import com.sync.entity.xubio.BancoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BancoRepository extends JpaRepository<BancoEntity, Long> {
    Optional<BancoEntity> findByXubioId(Long xubioId);
}
