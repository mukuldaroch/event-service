package com.daroch.event.services.impl;

import com.daroch.event.domain.entities.Event;
import com.daroch.event.exceptions.EventNotFoundException;
import com.daroch.event.exceptions.EventUpdateException;
import com.daroch.event.repositories.EventRepository;
import com.daroch.event.services.EventCommandService;
import com.daroch.event.dto.commands.CreateEventCommand;
import com.daroch.event.dto.commands.UpdateEventCommand;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventCommandServiceImpl implements EventCommandService {

  private final EventRepository eventRepository;

  /**
   * Creates a new event for a given organizer.
   *
   * <p>Sets up the event entity and its associated ticket types, linking each ticket type to the
   * parent event. The event and tickets are saved atomically via cascading.
   *
   * @param organizerId the UUID of the organizer creating the event; must not be null
   * @param eventRequest payload containing event details and ticket type information
   * @return the saved Event entity with ticket types
   * @throws UserNotFoundException if no user exists with the given organizerId
   */
  @Override
  @Transactional
  public Event createEvent(UUID organizerId, CreateEventCommand eventCommand) {

    Event eventToCreate = new Event();

    // Map event-level details from the request
    eventToCreate.setName(eventCommand.getName());
    eventToCreate.setStatus(eventCommand.getStatus());
    eventToCreate.setVenue(eventCommand.getVenue());
    eventToCreate.setEventStartDate(eventCommand.getEventStartDate());
    eventToCreate.setEventEndDate(eventCommand.getEventEndDate());
    eventToCreate.setSalesStartDate(eventCommand.getSalesStartDate());
    eventToCreate.setSalesEndDate(eventCommand.getSalesEndDate());
    eventToCreate.setOrganizerId(organizerId);

    // Save the event (cascades and saves ticket types automatically)
    Event saved = eventRepository.save(eventToCreate);

    // adding the event id to the returnDildo
    eventToCreate.setEventId(saved.getEventId());

    // return this shit
    return eventToCreate;
  }

  /**
   * Updates an existing event and its associated ticket types for a specific organizer.
   *
   * <p>Handles three cases for ticket types: creation of new ones (no ID), updating existing ones
   * (matching ID), and deletion of ticket types missing from the request.
   *
   * @param organizerId the UUID of the organizer; must not be null
   * @param eventId the UUID of the event to update; must match the ID in the request
   * @param event the update request payload containing updated event and ticket type data
   * @return the updated Event entity
   * @throws EventUpdateException if the request contains a null ID or mismatched event ID
   * @throws EventNotFoundException if no event exists with the given eventId for the organizer
   * @throws TicketTypeNotFoundException if a ticket type in the request does not exist in the event
   */
  @Override
  @Transactional
  public Event updateEventForOrganizer(UUID organizerId, UpdateEventCommand cmd) {

    if (cmd.getEventId() == null) {
      throw new EventUpdateException("Event ID cannot be null");
    }

    Event event =
        eventRepository
            .findByEventIdAndOrganizerId(cmd.getEventId(), organizerId)
            .orElseThrow(
                () ->
                    new EventNotFoundException(
                        "Event with id '" + cmd.getEventId() + "' does not exist"));

    if (cmd.getName() != null) {
      event.setName(cmd.getName());
    }
    if (cmd.getVenue() != null) {
      event.setVenue(cmd.getVenue());
    }
    if (cmd.getStatus() != null) {
      event.setStatus(cmd.getStatus());
    }
    if (cmd.getEventStartDate() != null) {
      event.setEventStartDate(cmd.getEventStartDate());
    }
    if (cmd.getEventEndDate() != null) {
      event.setEventEndDate(cmd.getEventEndDate());
    }
    if (cmd.getSalesStartDate() != null) {
      event.setSalesStartDate(cmd.getSalesStartDate());
    }
    if (cmd.getSalesEndDate() != null) {
      event.setSalesEndDate(cmd.getSalesEndDate());
    }

    return eventRepository.save(event);
  }

  /**
   * Deletes an event belonging to a specific organizer.
   *
   * <p>Performs a safe deletion by combining the event ID and organizer ID, ensuring that only
   * events belonging to the given organizer are deleted. If no event is deleted, an exception is
   * thrown.
   *
   * @param organizerId the UUID of the organizer; must not be null
   * @param eventId the UUID of the event to delete; must not be null
   * @throws IllegalArgumentException if either organizerId or eventId is null
   * @throws RuntimeException if no event was deleted (event not found or not owned by organizer)
   */
  @Override
  public void deleteEventForOrganizer(UUID organizerId, UUID eventId) {
    Optional<Event> event = eventRepository.findByEventIdAndOrganizerId(eventId, organizerId);

    event.ifPresent(eventRepository::delete);
  }
}
