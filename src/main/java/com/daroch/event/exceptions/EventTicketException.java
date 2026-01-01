package com.daroch.event.exceptions;

public class EventTicketException extends RuntimeException {

  // No-arg constructor
  public EventTicketException() {
    super();
  }

  // Constructor with message
  public EventTicketException(String message) {
    super(message);
  }

  // Constructor with message and cause
  public EventTicketException(String message, Throwable cause) {
    super(message, cause);
  }

  // Constructor with cause only
  public EventTicketException(Throwable cause) {
    super(cause);
  }

  // Full constructor (message, cause, enableSuppression, writableStackTrace)
  public EventTicketException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
