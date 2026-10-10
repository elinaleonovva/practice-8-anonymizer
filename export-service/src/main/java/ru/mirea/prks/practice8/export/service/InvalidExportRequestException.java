package ru.mirea.prks.practice8.export.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidExportRequestException extends RuntimeException {
    public InvalidExportRequestException(String message) { super(message); }
}
