package ru.mirea.prks.practice8.dataset.domain;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/** An approved source definition; this service stores dataset metadata only. */
@Entity
@Table(name = "datasets")
public class Dataset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String title;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dataset_allowed_columns", joinColumns = @JoinColumn(name = "dataset_id"))
    @Column(name = "column_name", nullable = false)
    private List<String> allowedColumns = new ArrayList<>();

    protected Dataset() {
    }

    public Dataset(String code, String title, List<String> allowedColumns) {
        this.code = code;
        this.title = title;
        this.allowedColumns = new ArrayList<>(allowedColumns);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public List<String> getAllowedColumns() { return new ArrayList<>(allowedColumns); }
}
