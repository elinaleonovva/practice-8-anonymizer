package ru.mirea.prks.practice8.export.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mirea.prks.practice8.export.service.ExportService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/exports")
public class ExportController {
    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @PostMapping
    public Mono<ResponseEntity<ExportJobResponse>> create(@Valid @RequestBody CreateExportRequest request,
                                                           @RequestHeader("X-Request-ID") String requestId) {
        return exportService.create(request, requestId)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @GetMapping("/{id}")
    public Mono<ExportJobResponse> findById(@PathVariable UUID id) {
        return exportService.findById(id);
    }

    @GetMapping(value = "/{id}/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<JobStatusEvent>> statusEvents(@PathVariable UUID id) {
        return exportService.statusEvents(id)
                .map(event -> ServerSentEvent.builder(event)
                        .event("export-status")
                        .id(event.getOccurredAt().toString())
                        .build());
    }
}
