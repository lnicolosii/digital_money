package com.digitalmoney.account_service.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ApplicationHandlerException extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        log.warn("Validation error: {}", ex.getMessage());

        Map<String, String> errorsMap = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorsMap.put(fieldName, errorMessage);
        });

        ExceptionResponse response = ExceptionResponse.builder()
                .date(LocalDateTime.now())
                .message("Validation failed")
                .details(errorsMap.toString())
                .errorCode("VALIDATION_ERROR")
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(NotFoundException ex, WebRequest request) {
        log.warn("Not found exception: {}", ex.getMessage());
        
        ExceptionResponse response = ExceptionResponse.builder()
                .date(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .errorCode("ACCOUNT_NOT_FOUND")
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequest(BadRequestException ex, WebRequest request) {
        log.warn("Bad request exception: {}", ex.getMessage());
        
        ExceptionResponse response = ExceptionResponse.builder()
                .date(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .errorCode("BAD_REQUEST")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleForbidden(ForbiddenException ex, WebRequest request) {
        log.warn("Forbidden exception: {}", ex.getMessage());
        
        ExceptionResponse response = ExceptionResponse.builder()
                .date(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .errorCode("FORBIDDEN")
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        log.error("Runtime exception occurred: {}", ex.getMessage(), ex);
        
        ExceptionResponse response = ExceptionResponse.builder()
                .date(LocalDateTime.now())
                .message("A runtime error occurred")
                .details(request.getDescription(false))
                .errorCode("RUNTIME_ERROR")
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        ExceptionResponse response = ExceptionResponse.builder()
                .date(LocalDateTime.now())
                .message("An unexpected error occurred")
                .details(request.getDescription(false))
                .errorCode("INTERNAL_SERVER_ERROR")
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
