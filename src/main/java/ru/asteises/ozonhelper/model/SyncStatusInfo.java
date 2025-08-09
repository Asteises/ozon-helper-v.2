package ru.asteises.ozonhelper.model;

import lombok.Builder;
import lombok.Data;
import ru.asteises.ozonhelper.enums.CatalogStatus;
import ru.asteises.ozonhelper.enums.SyncStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class SyncStatusInfo {

    private String taskId;
    private CatalogStatus status;
    private SyncStatus syncStatus;
    private int progress;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private String errorMessage;
}
