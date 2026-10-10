package ru.mirea.prks.practice8.export.service;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import ru.mirea.prks.practice8.export.api.JobStatusEvent;
import ru.mirea.prks.practice8.export.domain.ExportJob;

import java.time.Instant;
import java.util.UUID;

/** Bridges JPA state changes to the user's reactive SSE subscription. */
@Component
public class JobStatusPublisher {
    private final Sinks.Many<JobStatusEvent> updates = Sinks.many().multicast().onBackpressureBuffer();

    public void publish(ExportJob job) {
        updates.tryEmitNext(new JobStatusEvent(job.getId(), job.getStatus(), Instant.now()));
    }

    public Flux<JobStatusEvent> updatesFor(UUID jobId) {
        return updates.asFlux().filter(event -> jobId.equals(event.getJobId()));
    }
}
