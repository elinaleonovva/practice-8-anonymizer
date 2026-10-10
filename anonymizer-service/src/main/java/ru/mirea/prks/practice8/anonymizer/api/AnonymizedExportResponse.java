package ru.mirea.prks.practice8.anonymizer.api;

import ru.mirea.prks.practice8.anonymizer.domain.AnonymizedExport;

import java.time.Instant;
import java.util.UUID;

public class AnonymizedExportResponse {
    private final UUID jobId;
    private final String datasetCode;
    private final String summary;
    private final Instant processedAt;

    public AnonymizedExportResponse(AnonymizedExport export) {
        this.jobId = export.getJobId();
        this.datasetCode = export.getDatasetCode();
        this.summary = export.getSummary();
        this.processedAt = export.getProcessedAt();
    }

    public UUID getJobId() { return jobId; }
    public String getDatasetCode() { return datasetCode; }
    public String getSummary() { return summary; }
    public Instant getProcessedAt() { return processedAt; }
}
