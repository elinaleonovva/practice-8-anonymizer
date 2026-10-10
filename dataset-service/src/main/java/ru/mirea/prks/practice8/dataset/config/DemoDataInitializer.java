package ru.mirea.prks.practice8.dataset.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mirea.prks.practice8.dataset.domain.Dataset;
import ru.mirea.prks.practice8.dataset.repository.DatasetRepository;

import java.util.Arrays;

/** A small demo dataset replaces a connection to a real database containing personal data. */
@Configuration
public class DemoDataInitializer {
    @Bean
    CommandLineRunner loadDemoDataset(DatasetRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Dataset("customers", "Customers (demo dataset)",
                        Arrays.asList("fullName", "email", "birthDate", "phone")));
            } else {
                // Keep data from existing Docker volumes consistent with the English demo UI.
                repository.findByCode("customers").ifPresent(dataset -> {
                    dataset.updateTitle("Customers (demo dataset)");
                    repository.save(dataset);
                });
            }
        };
    }
}
