package ru.mirea.prks.practice8.anonymizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mirea.prks.practice8.anonymizer.domain.AnonymizedExport;

import java.util.UUID;

public interface AnonymizedExportRepository extends JpaRepository<AnonymizedExport, UUID> {
}
