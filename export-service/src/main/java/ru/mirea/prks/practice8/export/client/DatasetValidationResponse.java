package ru.mirea.prks.practice8.export.client;

import java.util.List;

/** A local HTTP contract copy keeps the services independent from each other's source code. */
public class DatasetValidationResponse {
    private boolean valid;
    private String reason;
    private List<String> acceptedColumns;

    public DatasetValidationResponse() {
    }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public List<String> getAcceptedColumns() { return acceptedColumns; }
    public void setAcceptedColumns(List<String> acceptedColumns) { this.acceptedColumns = acceptedColumns; }
}
