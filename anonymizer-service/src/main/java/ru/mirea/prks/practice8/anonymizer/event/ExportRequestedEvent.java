package ru.mirea.prks.practice8.anonymizer.event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** An independent copy of the ExportRequested message contract. */
public class ExportRequestedEvent {
    private UUID eventId;
    private UUID jobId;
    private String datasetCode;
    private List<String> columns = new ArrayList<>();
    private String requestId;

    public ExportRequestedEvent() { }
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
