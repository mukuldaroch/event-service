package com.daroch.event.services;

import com.daroch.event.domain.entities.Event;
import com.daroch.event.services.commands.CreateEventCommand;
import com.daroch.event.services.commands.UpdateEventCommand;
import java.util.UUID;

public interface EventCommandService {
  Event createEvent(UUID organizerId, CreateEventCommand event);

  Event updateEventForOrganizer(UUID organizerId, UUID eventId, UpdateEventCommand event);

  void deleteEventForOrganizer(UUID organizerId, UUID eventId);
}
