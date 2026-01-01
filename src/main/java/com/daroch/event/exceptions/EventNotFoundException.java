package com.daroch.event.exceptions;

public class EventNotFoundException extends RuntimeException {

  // No-arg constructor
  public EventNotFoundException() {
    super();
  }

  // Constructor with message
  public EventNotFoundException(String message) {
    super(message);
  }

  // Constructor with message and cause
  public EventNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  // Constructor with cause only
  public EventNotFoundException(Throwable cause) {
    super(cause);
  }

  // Full constructor
  public EventNotFoundException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
