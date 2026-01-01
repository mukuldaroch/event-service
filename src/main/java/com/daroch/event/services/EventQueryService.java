package com.daroch.event.services;

import com.daroch.event.domain.entities.Event;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventQueryService {

  Optional<Event> getEventForOrganizer(UUID organizerId, UUID eventId);

  Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable);

  Page<Event> listPublishedEvents(Pageable pageable);

  Page<Event> searchPublishedEvents(String query, Pageable pageable);

  Optional<Event> getPublishedEvent(UUID eventId);
}
