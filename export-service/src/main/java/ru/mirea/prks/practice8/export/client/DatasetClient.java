package ru.mirea.prks.practice8.export.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/** Synchronously validates the requested export columns before creating a job. */
@Component
public class DatasetClient {
    private final WebClient datasetWebClient;

    public DatasetClient(WebClient datasetWebClient) {
        this.datasetWebClient = datasetWebClient;
    }

    @CircuitBreaker(name = "datasetService", fallbackMethod = "unavailable")
    @Retry(name = "datasetService")
    public Mono<DatasetValidationResponse> validate(String datasetCode, List<String> columns, String requestId) {
        return datasetWebClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/internal/datasets/{code}/validation");
                    // Send each column as a separate ?columns=... parameter so Spring builds a List correctly.
                    columns.forEach(column -> uriBuilder.queryParam("columns", column));
                    return uriBuilder.build(datasetCode);
                })
                .header("X-Request-ID", requestId)
                .retrieve()
                .bodyToMono(DatasetValidationResponse.class);
    }

    public Mono<DatasetValidationResponse> unavailable(String datasetCode, List<String> columns,
                                                        String requestId, Throwable error) {
        return Mono.error(new DatasetServiceUnavailableException(
                "Dataset Service is temporarily unavailable. Please try again later.", error));
    }
}
