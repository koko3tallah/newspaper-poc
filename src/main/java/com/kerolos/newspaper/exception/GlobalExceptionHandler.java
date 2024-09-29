package com.kerolos.newspaper.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleAccessDeniedException(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
    }


    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleAuthenticationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    @ExceptionHandler({InvalidFormatException.class, JsonParseException.class})
    public ResponseEntity<?> handleInvalidFormatAndJsonParseException(Exception ex, WebRequest request) {
        ResponseEntity<Map<String, Object>> errorDetails = handleFormatException(ex);
        if (errorDetails != null) return errorDetails;
        return new ResponseEntity<>("Invalid format or parse error", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Map<String, Object>> handleFormatException(Throwable ex) {
        if (ex instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) ex;

            if (invalidFormatException.getTargetType().isEnum()) {
                String fieldName = invalidFormatException.getPath().get(0).getFieldName();
                String invalidValue = invalidFormatException.getValue().toString();

                String validValues = Arrays.stream(invalidFormatException.getTargetType().getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                Map<String, Object> errorDetails = new HashMap<>();
                errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
                errorDetails.put("error", "Invalid Enum Value");
                errorDetails.put("message", String.format("Invalid value '%s' for field '%s'. Accepted values are: [%s].",
                        invalidValue, fieldName, validValues));
                errorDetails.put("timestamp", System.currentTimeMillis());

                return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
            }
        }

        if (ex instanceof JsonParseException) {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
            errorDetails.put("error", "Malformed JSON");
            errorDetails.put("message", "The JSON input is not well-formed. Please check the syntax.");
            errorDetails.put("timestamp", System.currentTimeMillis());

            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        Throwable rootCause = ex.getMostSpecificCause();
        handleFormatException(rootCause);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        errorDetails.put("error", "Bad Request");
        errorDetails.put("message", "Invalid request payload");
        errorDetails.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleExceptions(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
