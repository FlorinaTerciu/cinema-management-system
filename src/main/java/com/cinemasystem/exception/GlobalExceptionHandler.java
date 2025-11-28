package com.cinemasystem.exception;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String URI_PREFIX = "urn:problem-type:";

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequest(BadRequestException ex) {
        return buildProblem(ex.getMessage(), "bad-request", "Bad Request", BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex) {
        return buildProblem(ex.getMessage(), "not-found", "Not Found", NOT_FOUND);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ProblemDetail handleInternal(InternalServerErrorException ex) {
        return buildProblem(ex.getMessage(), "server-error", "Server Error", INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail pd = buildProblem("Validation failed", "validation-error", "Validation Error", BAD_REQUEST);
        Map<String, String> errors = new HashMap<>();

        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }

        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        return buildProblem("Unexpected server error.", "server-error", "Server Error", INTERNAL_SERVER_ERROR);
    }

    private ProblemDetail buildProblem(String msg, String code, String title, HttpStatus status) {
        ProblemDetail detail = forStatusAndDetail(status, msg);
        detail.setType(URI.create(URI_PREFIX + code));
        detail.setTitle(title);
        return detail;
    }
}
