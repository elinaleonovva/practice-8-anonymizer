package ru.mirea.prks.practice8.dataset.api;

import java.util.List;

/** Contract for the internal Export Service -> Dataset Service HTTP call. */
public class DatasetValidationResponse {
    private final boolean valid;
    private final String reason;
    private final List<String> acceptedColumns;

    public DatasetValidationResponse(boolean valid, String reason, List<String> acceptedColumns) {
        this.valid = valid;
        this.reason = reason;
        this.acceptedColumns = acceptedColumns;
    }

    public boolean isValid() { return valid; }
    public String getReason() { return reason; }
    public List<String> getAcceptedColumns() { return acceptedColumns; }
}
