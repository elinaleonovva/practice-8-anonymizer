package ru.mirea.prks.practice8.export.api;

import ru.mirea.prks.practice8.export.domain.ExportStatus;

import java.time.Instant;
import java.util.UUID;

/** An SSE message sent to the user when an export job status changes. */
public class JobStatusEvent {
    private final UUID jobId;
    private final ExportStatus status;
    private final Instant occurredAt;

    public JobStatusEvent(UUID jobId, ExportStatus status, Instant occurredAt) {
        this.jobId = jobId;
        this.status = status;
        this.occurredAt = occurredAt;
    }

    public UUID getJobId() { return jobId; }
    public ExportStatus getStatus() { return status; }
    public Instant getOccurredAt() { return occurredAt; }
}
