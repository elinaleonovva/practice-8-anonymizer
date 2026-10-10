package ru.mirea.prks.practice8.export.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExportNotFoundException extends RuntimeException {
    public ExportNotFoundException() { super("Export job not found"); }
}
