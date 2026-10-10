package ru.mirea.prks.practice8.export.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class CreateExportRequest {
    @NotBlank(message = "datasetCode is required")
    private String datasetCode;

    @NotEmpty(message = "Select at least one column")
    private List<@NotBlank(message = "Column name must not be blank") String> columns;

    public String getDatasetCode() { return datasetCode; }
    public void setDatasetCode(String datasetCode) { this.datasetCode = datasetCode; }
    public List<String> getColumns() { return columns; }
    public void setColumns(List<String> columns) { this.columns = columns; }
}
