package ru.mirea.prks.practice8.anonymizer.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/** The eventId journal makes RabbitMQ message processing idempotent. */
@Entity
@Table(name = "handled_events")
public class HandledEvent {
    @Id
    private UUID eventId;
    private Instant handledAt;

    protected HandledEvent() {
    }

    public HandledEvent(UUID eventId) {
        this.eventId = eventId;
        this.handledAt = Instant.now();
    }
}
