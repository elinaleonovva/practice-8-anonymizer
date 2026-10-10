package ru.mirea.prks.practice8.anonymizer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.prks.practice8.anonymizer.config.RabbitTopologyConfig;
import ru.mirea.prks.practice8.anonymizer.domain.AnonymizedExport;
import ru.mirea.prks.practice8.anonymizer.domain.HandledEvent;
import ru.mirea.prks.practice8.anonymizer.event.ExportReadyEvent;
import ru.mirea.prks.practice8.anonymizer.event.ExportRequestedEvent;
import ru.mirea.prks.practice8.anonymizer.repository.AnonymizedExportRepository;
import ru.mirea.prks.practice8.anonymizer.repository.HandledEventRepository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AnonymizationListener {
    private static final Logger log = LoggerFactory.getLogger(AnonymizationListener.class);
    private final HandledEventRepository handledEventRepository;
    private final AnonymizedExportRepository anonymizedExportRepository;
    private final RabbitTemplate rabbitTemplate;

    public AnonymizationListener(HandledEventRepository handledEventRepository,
                                 AnonymizedExportRepository anonymizedExportRepository,
                                 RabbitTemplate rabbitTemplate) {
        this.handledEventRepository = handledEventRepository;
        this.anonymizedExportRepository = anonymizedExportRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    @RabbitListener(queues = RabbitTopologyConfig.REQUESTED_QUEUE)
    public void anonymize(ExportRequestedEvent event) {
        if (handledEventRepository.existsById(event.getEventId())) {
            log.info("[{}] duplicate ExportRequested ignored: eventId={}", event.getRequestId(), event.getEventId());
            return;
        }

        // Save eventId first so a repeated delivery cannot create a second result.
        handledEventRepository.save(new HandledEvent(event.getEventId()));
        String summary = makeSummary(event);
        anonymizedExportRepository.save(new AnonymizedExport(event.getJobId(), event.getDatasetCode(), summary));

        ExportReadyEvent ready = new ExportReadyEvent(UUID.randomUUID(), event.getJobId(), summary, event.getRequestId());
        rabbitTemplate.convertAndSend(RabbitTopologyConfig.EXCHANGE, "export.ready", ready);
        log.info("[{}] event ExportRequested received and ExportReady sent: requestEventId={}, readyEventId={}, jobId={}",
                event.getRequestId(), event.getEventId(), ready.getEventId(), event.getJobId());
    }

    /** Demo logic: no real personal data is stored, only the applied rules are shown. */
    private String makeSummary(ExportRequestedEvent event) {
        Map<String, String> rules = new LinkedHashMap<>();
        rules.put("fullName", "A. B.");
        rules.put("email", "i***@example.test");
        rules.put("phone", "+1 *** ***-**-42");
        rules.put("birthDate", "1980-01-01 (generalized)");

        return event.getColumns().stream()
                .map(column -> column + " -> " + rules.getOrDefault(column, "<redacted>"))
                .collect(Collectors.joining("; "));
    }
}
