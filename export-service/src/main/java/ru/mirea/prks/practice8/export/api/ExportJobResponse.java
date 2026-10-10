package ru.mirea.prks.practice8.export.api;

import ru.mirea.prks.practice8.export.domain.ExportJob;
import ru.mirea.prks.practice8.export.domain.ExportStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ExportJobResponse {
    private final UUID id;
    private final String datasetCode;
    private final List<String> columns;
    private final ExportStatus status;
    private final Instant createdAt;
    private final String anonymizationSummary;

    public ExportJobResponse(ExportJob job) {
        this.id = job.getId();
        this.datasetCode = job.getDatasetCode();
        this.columns = job.getColumns();
        this.status = job.getStatus();
        this.createdAt = job.getCreatedAt();
        this.anonymizationSummary = job.getAnonymizationSummary();
    }

    public UUID getId() { return id; }
    public String getDatasetCode() { return datasetCode; }
    public List<String> getColumns() { return columns; }
    public ExportStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public String getAnonymizationSummary() { return anonymizationSummary; }
}
