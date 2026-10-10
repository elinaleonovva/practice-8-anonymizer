package ru.mirea.prks.practice8.dataset.api;

import java.util.List;

public class DatasetResponse {
    private final String code;
    private final String title;
    private final List<String> allowedColumns;

    public DatasetResponse(String code, String title, List<String> allowedColumns) {
        this.code = code;
        this.title = title;
        this.allowedColumns = allowedColumns;
    }

    public String getCode() { return code; }
    public String getTitle() { return title; }
    public List<String> getAllowedColumns() { return allowedColumns; }
}
