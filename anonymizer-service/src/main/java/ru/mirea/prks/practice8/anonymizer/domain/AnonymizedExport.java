package ru.mirea.prks.practice8.anonymizer.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/** An anonymization result owned exclusively by Anonymizer Service. */
@Entity
@Table(name = "anonymized_exports")
public class AnonymizedExport {
    @Id
    private UUID jobId;

    @Column(nullable = false)
    private String datasetCode;

    @Column(nullable = false, length = 1000)
    private String summary;

    @Column(nullable = false)
    private Instant processedAt;

    protected AnonymizedExport() {
    }

    public AnonymizedExport(UUID jobId, String datasetCode, String summary) {
        this.jobId = jobId;
        this.datasetCode = datasetCode;
        this.summary = summary;
        this.processedAt = Instant.now();
    }

    public UUID getJobId() { return jobId; }
    public String getDatasetCode() { return datasetCode; }
    public String getSummary() { return summary; }
    public Instant getProcessedAt() { return processedAt; }
}
