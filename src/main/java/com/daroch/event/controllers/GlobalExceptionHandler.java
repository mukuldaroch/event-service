package com.daroch.event.controllers;

import com.daroch.event.dto.response.ErrorResponse;
import com.daroch.event.exceptions.BusinessException;
import com.daroch.event.exceptions.EventUpdateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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
  public ResponseEntity<ErrorResponse> handleEventUpdateException(
      EventUpdateException ex, HttpServletRequest request) {

    log.error("Caught EventUpdateException", ex);

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .errorCode("EVENT_UPDATE_FAILED")
            .message("Unable to update event")
            .path(request.getRequestURI())
            .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  /**
   * Handles cases where a requested event does not exist.
   *
   * @param ex the exception indicating the event was not found
   * @return an error response with HTTP 400
   */
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(
      BusinessException ex, HttpServletRequest request) {

    log.error("Caught BusinessException", ex);

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(ex.getStatus().value())
            .errorCode(ex.getErrorCode())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();

    return ResponseEntity.status(ex.getStatus()).body(errorResponse);
  }

  /**
   * Handles validation errors triggered by @Valid or @Validated on request bodies.
   *
   * @param ex the exception containing validation failure details
   * @return an error response with HTTP 400
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {

    log.error("Caught MethodArgumentNotValidException", ex);

    String errorMessage =
        ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .orElse("Validation failed");

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .errorCode("VALIDATION_ERROR")
            .message(errorMessage)
            .path(request.getRequestURI())
            .build();

    return ResponseEntity.badRequest().body(errorResponse);
  }

  /**
   * Handles validation errors triggered by @Validated on parameters or path variables.
   *
   * @param ex the exception containing constraint violation details
   * @return an error response with HTTP 400
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(
      ConstraintViolationException ex, HttpServletRequest request) {

    log.error("Caught ConstraintViolationException", ex);

    String errorMessage =
        ex.getConstraintViolations().stream()
            .findFirst()
            .map(v -> v.getPropertyPath() + ": " + v.getMessage())
            .orElse("Constraint violation occurred");

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .errorCode("CONSTRAINT_VIOLATION")
            .message(errorMessage)
            .path(request.getRequestURI())
            .build();

    return ResponseEntity.badRequest().body(errorResponse);
  }

  /**
   * Handles requests to non-existent endpoints.
   *
   * @param ex the exception indicating no matching endpoint/resource exists
   * @return an error response with HTTP 404
   */
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoResourceFoundException(
      NoResourceFoundException ex, HttpServletRequest request) {

    log.warn("No endpoint found: {}", request.getRequestURI());

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.NOT_FOUND.value())
            .errorCode("ENDPOINT_NOT_FOUND")
            .message("Endpoint not found")
            .path(request.getRequestURI())
            .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  /**
   * Handles any unexpected or unhandled exceptions.
   *
   * @param ex the exception that was not caught by other handlers
   * @return an error response with HTTP 500
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {

    log.error("Caught unexpected exception", ex);

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .errorCode("INTERNAL_SERVER_ERROR")
            .message("An unexpected error occurred")
            .path(request.getRequestURI())
            .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
