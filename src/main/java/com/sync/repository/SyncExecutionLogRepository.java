package com.sync.repository;

import com.sync.entity.SyncExecutionLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SyncExecutionLogRepository extends JpaRepository<SyncExecutionLogEntity, Long> {
    List<SyncExecutionLogEntity> findBySourceAndEntityTypeOrderByExecutedAtDesc(
            String source, String entityType);
    List<SyncExecutionLogEntity> findByExecutedAtAfter(LocalDateTime from);
}
