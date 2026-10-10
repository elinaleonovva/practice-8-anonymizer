package ru.mirea.prks.practice8.dataset.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mirea.prks.practice8.dataset.domain.Dataset;

import java.util.Optional;

public interface DatasetRepository extends JpaRepository<Dataset, Long> {
    Optional<Dataset> findByCode(String code);
}
