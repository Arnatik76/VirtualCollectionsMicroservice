package kz.example.controller;

import kz.example.exception.ResourceAlreadyExistsException;
import kz.example.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError; // Импорт
import org.springframework.web.bind.MethodArgumentNotValidException; // Импорт
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.List; // Импорт
import java.util.stream.Collectors; // Импорт

@ControllerAdvice
public class GlobalExceptionHandler {

    // Обработчик для ошибки "Ресурс уже существует" (Conflict)
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<?> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", "Conflict"); // Указываем более точный тип ошибки
        errorDetails.put("message", ex.getMessage()); // Сообщение из исключения (например, "Username '...' already exists")

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT); // Возвращаем 409 Conflict
    }

    // Обработчик для ошибок валидации (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        // Возвращаем 400 Bad Request с деталями ошибок по полям
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Опционально: общий обработчик для других RuntimeExceptions
    // @ExceptionHandler(RuntimeException.class)
    // public ResponseEntity<?> handleGenericRuntimeException(RuntimeException ex) {
    //     Map<String, String> errorDetails = new HashMap<>();
    //     errorDetails.put("error", "Internal Server Error");
    //     errorDetails.put("message", "An unexpected error occurred."); // Не раскрывайте детали внутреннего исключения
    //     // Логировать ex.getMessage() и stack trace на сервере
    //     ex.printStackTrace(); // В продакшене используйте логгер
    //     return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR); // Возвращаем 500
    // }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", "Not Found");
        errorDetails.put("message", ex.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}