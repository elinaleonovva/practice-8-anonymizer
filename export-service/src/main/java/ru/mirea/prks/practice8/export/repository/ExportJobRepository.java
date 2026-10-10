package ru.mirea.prks.practice8.export.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mirea.prks.practice8.export.domain.ExportJob;

import java.util.UUID;

public interface ExportJobRepository extends JpaRepository<ExportJob, UUID> {
}
