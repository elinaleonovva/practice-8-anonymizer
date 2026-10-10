package ru.mirea.prks.practice8.export.event;

import java.util.UUID;

public class ExportReadyEvent {
    private UUID eventId;
    private UUID jobId;
    private String summary;
    private String requestId;

    public ExportReadyEvent() {
    }

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }
    public UUID getJobId() { return jobId; }
    public void setJobId(UUID jobId) { this.jobId = jobId; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
}
