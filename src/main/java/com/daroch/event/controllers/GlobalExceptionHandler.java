package com.daroch.event.controllers;

import com.daroch.event.dtos.ErrorDto;
import com.daroch.event.exceptions.EventNotFoundException;
import com.daroch.event.exceptions.EventUpdateException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
// Tells Spring this class will globally handle exceptions for REST controllers
@Slf4j // Adds a logger named 'log' using Lombok
public class GlobalExceptionHandler {

  /**
   * Handles failures that occur while updating an event.
   *
   * @param ex the exception indicating the event update failed
   * @return an error response with HTTP 400
   */
  @ExceptionHandler(EventUpdateException.class)
  public ResponseEntity<ErrorDto> handleEventUpdateException(EventUpdateException ex) {
    log.error("Caught EventUpdateException", ex);

    ErrorDto errorDto = new ErrorDto();
    errorDto.setError("Unable to update event");

    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles cases where a requested event does not exist.
   *
   * @param ex the exception indicating the event was not found
   * @return an error response with HTTP 400
   */
  @ExceptionHandler(EventNotFoundException.class)
  public ResponseEntity<ErrorDto> handleEventNotFoundException(EventNotFoundException ex) {
    log.error("Caught EventNotFoundException", ex);

    ErrorDto errorDto = new ErrorDto();
    errorDto.setError("Event not found");

    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles validation errors triggered by @Valid or @Validated on request bodies.
   *
   * @param ex the exception containing validation failure details
   * @return an error response with HTTP 400
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    log.error("Caught MethodArgumentNotValidException", ex);

    ErrorDto errorDto = new ErrorDto();

    BindingResult bindingResult = ex.getBindingResult();
    List<FieldError> fieldErrors = bindingResult.getFieldErrors();

    String errorMessage =
        fieldErrors.stream()
            .findFirst()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .orElse("Validation error occurred");

    errorDto.setError(errorMessage);
    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles validation errors triggered by @Validated on parameters or path variables.
   *
   * @param ex the exception containing constraint violation details
   * @return an error response with HTTP 400
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException ex) {
    log.error("Caught ConstraintViolationException", ex);

    ErrorDto errorDto = new ErrorDto();

    String errorMessage =
        ex.getConstraintViolations().stream()
            .findFirst()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .orElse("Constraint violation occurred");

    errorDto.setError(errorMessage);
    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles any unexpected or unhandled exceptions.
   *
   * @param ex the exception that was not caught by other handlers
   * @return an error response with HTTP 500
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleException(Exception ex) {
    log.error("Caught exception", ex);

    ErrorDto errorDto = new ErrorDto();
    errorDto.setError("An unknown error occurred");

    return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
