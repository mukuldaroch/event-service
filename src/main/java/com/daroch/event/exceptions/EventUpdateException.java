package com.daroch.event.exceptions;

public class EventUpdateException extends RuntimeException {

  // No-arg constructor
  public EventUpdateException() {
    super();
  }

  // Constructor with message
  public EventUpdateException(String message) {
    super(message);
  }

  // Constructor with message and cause
  public EventUpdateException(String message, Throwable cause) {
    super(message, cause);
  }

  // Constructor with cause only
  public EventUpdateException(Throwable cause) {
    super(cause);
  }

  // Full constructor
  public EventUpdateException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
