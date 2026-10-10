package ru.mirea.prks.practice8.dataset.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.mirea.prks.practice8.dataset.domain.Dataset;
import ru.mirea.prks.practice8.dataset.repository.DatasetRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class DatasetController {
    private static final Logger log = LoggerFactory.getLogger(DatasetController.class);
    private final DatasetRepository repository;

    public DatasetController(DatasetRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/datasets")
    public List<DatasetResponse> findAll() {
        return repository.findAll().stream()
                .map(dataset -> new DatasetResponse(dataset.getCode(), dataset.getTitle(), dataset.getAllowedColumns()))
                .collect(Collectors.toList());
    }

    /** This internal endpoint is not routed by the gateway and is called by Export Service only. */
    @GetMapping("/internal/datasets/{code}/validation")
    public DatasetValidationResponse validate(@PathVariable String code,
                                              @RequestParam List<String> columns,
                                              @RequestHeader("X-Request-ID") String requestId) {
        Dataset dataset = repository.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dataset not found"));
        boolean valid = !columns.isEmpty() && dataset.getAllowedColumns().containsAll(columns);
        String reason = valid ? "Columns are allowed" : "Unknown or forbidden columns were requested";
        log.info("[{}] dataset validation: code={}, columns={}, valid={}", requestId, code, columns, valid);
        return new DatasetValidationResponse(valid, reason, columns);
    }
}
