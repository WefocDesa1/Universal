package com.sync.service;

import com.sync.entity.SyncExecutionLogEntity;
import com.sync.repository.SyncExecutionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncLogService {

    private final SyncExecutionLogRepository logRepository;

    public SyncExecutionLogEntity startLog(String source, String entityType) {
        SyncExecutionLogEntity entry = SyncExecutionLogEntity.builder()
                .source(source)
                .entityType(entityType)
                .status("RUNNING")
                .executedAt(LocalDateTime.now())
                .build();
        return logRepository.save(entry);
    }

    public void completeLog(SyncExecutionLogEntity log, int fetched, int created, int updated, long durationMs) {
        log.setStatus("SUCCESS");
        log.setRecordsFetched(fetched);
        log.setRecordsCreated(created);
        log.setRecordsUpdated(updated);
        log.setDurationMs(durationMs);
        logRepository.save(log);
    }

    public void failLog(SyncExecutionLogEntity log, String errorMessage, long durationMs) {
        log.setStatus("FAILED");
        log.setErrorMessage(errorMessage);
        log.setDurationMs(durationMs);
        logRepository.save(log);
    }
}
