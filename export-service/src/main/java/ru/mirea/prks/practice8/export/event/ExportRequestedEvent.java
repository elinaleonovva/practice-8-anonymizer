package ru.mirea.prks.practice8.export.event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** The event contains only data required by the anonymization service. */
public class ExportRequestedEvent {
    private UUID eventId;
    private UUID jobId;
    private String datasetCode;
    private List<String> columns = new ArrayList<>();
    private String requestId;

    public ExportRequestedEvent() {
    }

    public ExportRequestedEvent(UUID eventId, UUID jobId, String datasetCode, List<String> columns, String requestId) {
        this.eventId = eventId;
        this.jobId = jobId;
        this.datasetCode = datasetCode;
        this.columns = new ArrayList<>(columns);
        this.requestId = requestId;
    }

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }
    public UUID getJobId() { return jobId; }
    public void setJobId(UUID jobId) { this.jobId = jobId; }
    public String getDatasetCode() { return datasetCode; }
    public void setDatasetCode(String datasetCode) { this.datasetCode = datasetCode; }
    public List<String> getColumns() { return columns; }
    public void setColumns(List<String> columns) { this.columns = columns; }
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
}
