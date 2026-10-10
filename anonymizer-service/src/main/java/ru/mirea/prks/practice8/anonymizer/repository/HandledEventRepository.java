package ru.mirea.prks.practice8.anonymizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mirea.prks.practice8.anonymizer.domain.HandledEvent;

import java.util.UUID;

public interface HandledEventRepository extends JpaRepository<HandledEvent, UUID> {
}
