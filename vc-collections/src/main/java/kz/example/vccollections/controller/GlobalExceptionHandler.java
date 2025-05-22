package kz.example.vccollections.controller;

import kz.example.vccollections.exception.ForbiddenActionException;
import kz.example.vccollections.exception.ResourceAlreadyExistsException;
import kz.example.vccollections.exception.ResourceNotFoundException;
import kz.example.vccollections.exception.UserNotAuthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;


import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), "Not Found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<Map<String, String>> handleUserNotAuthenticatedException(UserNotAuthenticatedException ex) {
        logger.warn("User not authenticated: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), "Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        logger.warn("Resource already exists: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), "Conflict", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<Map<String, String>> handleForbiddenActionException(ForbiddenActionException ex) {
        logger.warn("Forbidden action: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), "Forbidden", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class) // From Spring Security @PreAuthorize
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        logger.warn("Access denied: {}", ex.getMessage());
        return buildErrorResponse("You do not have permission to perform this action.", "Forbidden", HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        logger.warn("Validation errors: {}", errors);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", "Validation Failed");
        responseBody.put("messages", errors);
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Illegal argument: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), "Bad Request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException ex) {
        logger.warn("ResponseStatusException: Status {}, Reason {}", ex.getStatusCode(), ex.getReason());
        return buildErrorResponse(ex.getReason(), ex.getStatusCode().toString(), HttpStatus.valueOf(ex.getStatusCode().value()));
    }


    @ExceptionHandler(Exception.class) // Generic fallback
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        return buildErrorResponse("An unexpected internal server error occurred.", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, String>> buildErrorResponse(String message, String errorType, HttpStatus status) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", errorType);
        errorDetails.put("message", message);
        return new ResponseEntity<>(errorDetails, status);
    }
}