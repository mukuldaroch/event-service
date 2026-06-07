package com.daroch.event.exceptions;

import org.springframework.http.HttpStatus;

public class EventNotFoundException extends BusinessException {

  public EventNotFoundException() {
    super("EVENT_NOT_FOUND", "Event not found", HttpStatus.NOT_FOUND);
  }
}
