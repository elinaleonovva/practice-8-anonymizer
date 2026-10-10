package ru.mirea.prks.practice8.anonymizer.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.mirea.prks.practice8.anonymizer.repository.AnonymizedExportRepository;

import java.util.UUID;

@RestController
@RequestMapping("/api/anonymizations")
public class AnonymizationController {
    private final AnonymizedExportRepository repository;

    public AnonymizationController(AnonymizedExportRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{jobId}")
    public AnonymizedExportResponse findByJobId(@PathVariable UUID jobId) {
        return repository.findById(jobId)
                .map(AnonymizedExportResponse::new)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The result has not been generated yet"));
    }
}
