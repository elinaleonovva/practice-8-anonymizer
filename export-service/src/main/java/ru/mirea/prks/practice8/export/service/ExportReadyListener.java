package ru.mirea.prks.practice8.export.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.prks.practice8.export.config.RabbitTopologyConfig;
import ru.mirea.prks.practice8.export.domain.ExportJob;
import ru.mirea.prks.practice8.export.event.ExportReadyEvent;
import ru.mirea.prks.practice8.export.repository.ExportJobRepository;

@Component
public class ExportReadyListener {
    private static final Logger log = LoggerFactory.getLogger(ExportReadyListener.class);
    private final ExportJobRepository repository;
    private final JobStatusPublisher statusPublisher;

    public ExportReadyListener(ExportJobRepository repository, JobStatusPublisher statusPublisher) {
        this.repository = repository;
        this.statusPublisher = statusPublisher;
    }

    @Transactional
    @RabbitListener(queues = RabbitTopologyConfig.READY_QUEUE)
    public void onReady(ExportReadyEvent event) {
        repository.findById(event.getJobId()).ifPresent(job -> markReady(job, event));
    }

    private void markReady(ExportJob job, ExportReadyEvent event) {
        // A repeated ExportReady is safe: READY is replaced with the same result and no duplicate is created.
        job.markReady(event.getSummary());
        repository.save(job);
        statusPublisher.publish(job);
        log.info("[{}] event ExportReady received: eventId={}, jobId={}",
                event.getRequestId(), event.getEventId(), event.getJobId());
    }
}
