package com.sky.assessment.exception;

import com.sky.assessment.controller.response.ErrorResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {

    List<String> details = ex.getBindingResult()
        .getAllErrors()
        .stream()
        .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.toList());

    ErrorResponse errorResponse = new ErrorResponse("Validation Failed", details);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .message(ex.getMessage())
        .details(Collections.singletonList("Resource not found"))
        .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .message(ex.getMessage())
        .details(Collections.singletonList("User already exists with the provided email"))
        .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }
}
