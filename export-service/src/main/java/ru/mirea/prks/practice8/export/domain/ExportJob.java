package ru.mirea.prks.practice8.export.domain;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** An export job owned exclusively by Export Service. */
@Entity
@Table(name = "export_jobs")
public class ExportJob {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String datasetCode;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "export_job_columns", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "column_name", nullable = false)
    private List<String> columns = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExportStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    private String anonymizationSummary;

    protected ExportJob() {
    }

    public ExportJob(UUID id, String datasetCode, List<String> columns) {
        this.id = id;
        this.datasetCode = datasetCode;
        this.columns = new ArrayList<>(columns);
        this.status = ExportStatus.CREATED;
        this.createdAt = Instant.now();
    }

    public void markProcessing() { this.status = ExportStatus.PROCESSING; }
    public void markReady(String summary) { this.status = ExportStatus.READY; this.anonymizationSummary = summary; }
    public UUID getId() { return id; }
    public String getDatasetCode() { return datasetCode; }
    public List<String> getColumns() { return new ArrayList<>(columns); }
    public ExportStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public String getAnonymizationSummary() { return anonymizationSummary; }
}
