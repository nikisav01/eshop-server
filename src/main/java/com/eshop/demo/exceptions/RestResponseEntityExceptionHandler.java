package com.eshop.demo.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {EntityStateException.class})
    public ResponseEntity<Object> handleEntityState(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "[EntityStateException] Message: " + ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {EntityNotFound.class})
    public ResponseEntity<Object> handleEntityNotFound(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "[EntityNotFound] Message: " + ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {IncorrectRequest.class})
    public ResponseEntity<Object> handleIncorrectRequest(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "[IncorrectRequest] Message: " + ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {UnprocessableRequest.class})
    public ResponseEntity<Object> handleUnprocessableRequest(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "[UnprocessableRequest] Message: " + ex.getMessage(), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

}
