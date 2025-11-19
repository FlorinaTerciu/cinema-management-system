package com.cinemasystem.exception;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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
    public ProblemDetail handleInternalError(InternalServerErrorException ex) {
        return buildProblem(ex.getMessage(), "server-error", "Server Error", INTERNAL_SERVER_ERROR);
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
