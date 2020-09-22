
package com.rakuten.buildcharacterbackend.api.v1;

import java.util.HashMap;
import java.util.NoSuchElementException;

import com.rakuten.buildcharacterbackend.api.v1.exception.TTlOutOfRangeException;
import com.rakuten.buildcharacterbackend.domain.dto.response.error.ErrorCause;
import com.rakuten.buildcharacterbackend.domain.dto.response.error.ErrorResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * CustomizeExceptions
 */
@Slf4j
@ControllerAdvice
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage());
        return ErrorResponse.build(status, ErrorCause.BAD_REQUEST, createReadbleMessageError(ex.getBindingResult()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage());
        return ErrorResponse.build(status, ErrorCause.BAD_REQUEST, "Message not readble!");
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        log.info(ex.getMessage());
        return ErrorResponse.build(status, ErrorCause.BACKEND_FAILURE, "Internal error!");
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        log.info(ex.getMessage());
        return ErrorResponse.build(HttpStatus.UNAUTHORIZED, ErrorCause.UNAUTHORIZED, "Access Denied!");
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<Object> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        log.info(ex.getMessage());
        return ErrorResponse.build(HttpStatus.BAD_REQUEST, ErrorCause.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(TTlOutOfRangeException.class)
    protected ResponseEntity<Object> handleTTlOutOfRangeException(TTlOutOfRangeException ex) {
        log.info(ex.getMessage());
        return ErrorResponse.build(HttpStatus.BAD_REQUEST, ErrorCause.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        log.info(ex.getMessage());
        return ErrorResponse.build(HttpStatus.NOT_FOUND, ErrorCause.NOT_FOUND, "Id not found on database!");
    }

    @ExceptionHandler(JedisConnectionException.class)
    protected ResponseEntity<Object> handleJedisConnectionException(JedisConnectionException ex) {
        log.info(ex.getMessage());
        return ErrorResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCause.BACKEND_FAILURE, "Internal error!");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.info(ex.getMessage());
        return ErrorResponse.build(HttpStatus.BAD_REQUEST, ErrorCause.BAD_REQUEST,
                "Query Parameter passed with error!");
    }

    @ExceptionHandler(FeignException.class)
    protected ResponseEntity<Object> handleFeignException(FeignException ex) {
        log.info(ex.getMessage());

        switch (ex.status()) {
            case 500:
                return ErrorResponse.build(HttpStatus.SERVICE_UNAVAILABLE, ErrorCause.SERVICE_UNAVAILABLE,
                        "The consumed is down!");
            case 404:
                return ErrorResponse.build(HttpStatus.NOT_FOUND, ErrorCause.NOT_FOUND, "Not found!");
            default:
                return ErrorResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCause.BACKEND_FAILURE,
                        "Internal error!");
        }
    }

    private String createReadbleMessageError(BindingResult bindingResult) {
        HashMap<String, String> errors = new HashMap<String, String>();

        for (final FieldError error : bindingResult.getFieldErrors()) {
            errors.merge(error.getField(), error.getDefaultMessage(),
                    (oldMsg, newMsg) -> String.format("%s; %s", oldMsg, newMsg));
        }
        for (final ObjectError error : bindingResult.getGlobalErrors()) {
            errors.merge(error.getObjectName(), error.getDefaultMessage(),
                    (oldMsg, newMsg) -> String.format("%s; %s", oldMsg, newMsg));
        }

        return errors.toString();
    }
}