package com.bruno.productregistration.exceptions;

import com.bruno.productregistration.services.exceptions.ExistingResourceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExistingResourceException.class)
    public ResponseEntity<StandardError> existingResource(
            HttpServletRequest request, ExistingResourceException exception
    ) {
        Instant timestamp = Instant.now();
        int status = HttpStatus.BAD_REQUEST.value();
        String error = "Bad Request";
        StandardError standardError = StandardError.builder()
                .timestamp(timestamp)
                .status(status)
                .message(exception.getMessage())
                .error(error)
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validation(MethodArgumentNotValidException exception) {
        Instant timestamp = Instant.now();
        int status = HttpStatus.BAD_REQUEST.value();
        String message = "Validation error";
        ValidationError validationError = ValidationError.builder()
                .timestamp(timestamp)
                .status(status)
                .message(message)
                .build();
        for (FieldError field : exception.getBindingResult().getFieldErrors()){
            validationError.addError(field.getField(), field.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(validationError);
    }
}
