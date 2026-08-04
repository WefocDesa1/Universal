package com.sync.repository.magiis;

import com.sync.entity.magiis.MagiisViajeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MagiisViajeRepository extends JpaRepository<MagiisViajeEntity, Long> {
    Optional<MagiisViajeEntity> findByTravelIdForCarrier(Long travelIdForCarrier);
    boolean existsByTravelIdForCarrier(Long travelIdForCarrier);
}
