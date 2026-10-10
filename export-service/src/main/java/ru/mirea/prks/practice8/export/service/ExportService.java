package ru.mirea.prks.practice8.export.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.mirea.prks.practice8.export.api.CreateExportRequest;
import ru.mirea.prks.practice8.export.api.ExportJobResponse;
import ru.mirea.prks.practice8.export.api.JobStatusEvent;
import ru.mirea.prks.practice8.export.client.DatasetClient;
import ru.mirea.prks.practice8.export.config.RabbitTopologyConfig;
import ru.mirea.prks.practice8.export.domain.ExportJob;
import ru.mirea.prks.practice8.export.event.ExportRequestedEvent;
import ru.mirea.prks.practice8.export.repository.ExportJobRepository;

import java.time.Instant;
import java.util.UUID;

@Service
public class ExportService {
    private static final Logger log = LoggerFactory.getLogger(ExportService.class);
    private final DatasetClient datasetClient;
    private final ExportJobRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final JobStatusPublisher statusPublisher;

    public ExportService(DatasetClient datasetClient, ExportJobRepository repository,
                         RabbitTemplate rabbitTemplate, JobStatusPublisher statusPublisher) {
        this.datasetClient = datasetClient;
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.statusPublisher = statusPublisher;
    }

    public Mono<ExportJobResponse> create(CreateExportRequest request, String requestId) {
        return datasetClient.validate(request.getDatasetCode(), request.getColumns(), requestId)
                .flatMap(validation -> {
                    if (!validation.isValid()) {
                        return Mono.error(new InvalidExportRequestException(validation.getReason()));
                    }
                    return Mono.fromCallable(() -> {
                                ExportJob job = new ExportJob(UUID.randomUUID(), request.getDatasetCode(), request.getColumns());
                                job.markProcessing();
                                return repository.save(job);
                            })
                            // JPA blocks a thread, so this work runs outside the WebFlux event loop.
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(job -> publishRequested(job, requestId))
                            .map(ExportJobResponse::new);
                });
    }

    public Mono<ExportJobResponse> findById(UUID id) {
        return Mono.fromCallable(() -> repository.findById(id)
                        .orElseThrow(ExportNotFoundException::new))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ExportJobResponse::new);
    }

    public Flux<JobStatusEvent> statusEvents(UUID id) {
        return findById(id)
                .flatMapMany(job -> Flux.concat(
                        Mono.just(new JobStatusEvent(job.getId(), job.getStatus(), Instant.now())),
                        statusPublisher.updatesFor(id)));
    }

    private Mono<ExportJob> publishRequested(ExportJob job, String requestId) {
        return Mono.fromCallable(() -> {
                    ExportRequestedEvent event = new ExportRequestedEvent(
                            UUID.randomUUID(), job.getId(), job.getDatasetCode(), job.getColumns(), requestId);
                    rabbitTemplate.convertAndSend(RabbitTopologyConfig.EXCHANGE, "export.requested", event);
                    statusPublisher.publish(job);
                    log.info("[{}] event ExportRequested sent: eventId={}, jobId={}",
                            requestId, event.getEventId(), job.getId());
                    return job;
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}
