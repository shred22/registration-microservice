package com.registration.handler;


import org.jsoup.HttpStatusException;
import org.openapitools.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.time.OffsetDateTime;

@RestControllerAdvice
public class HttpControllerAdvice {

    @ExceptionHandler(value = HttpStatusException.class)
    public ResponseEntity<ErrorResponse> handleGenericNotFoundException(HttpServletRequest request, HttpServletResponse response, HttpStatusException e) {

        ErrorResponse error = new ErrorResponse();
        error.setFaultId((long) e.hashCode());
        error.setTimestamp(OffsetDateTime.now());
        error.setStatus(e.getStatusCode());
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(value = SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(HttpServletRequest request, HttpServletResponse response, SQLException e) {

        ErrorResponse error = new ErrorResponse();
        error.setFaultId((long) e.hashCode());
        error.setTimestamp(OffsetDateTime.now());
        error.setStatus((HttpStatus.INTERNAL_SERVER_ERROR.value()));
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(HttpServletRequest request, HttpServletResponse response, EntityNotFoundException e) {

        ErrorResponse error = new ErrorResponse();
        error.setFaultId((long) e.hashCode());
        error.setTimestamp(OffsetDateTime.now());
        error.setStatus((HttpStatus.NOT_FOUND.value()));
        error.setMessage("Requested Entity Not found");
        error.setPath(request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupportedException(HttpServletRequest request, HttpServletResponse response, HttpRequestMethodNotSupportedException e) {


        ErrorResponse error = new ErrorResponse();
        error.setFaultId((long) e.hashCode());
        error.setTimestamp(OffsetDateTime.now());
        error.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);

    }

}
