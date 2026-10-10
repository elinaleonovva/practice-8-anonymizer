package ru.mirea.prks.practice8.export.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class DatasetServiceUnavailableException extends RuntimeException {
    public DatasetServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
