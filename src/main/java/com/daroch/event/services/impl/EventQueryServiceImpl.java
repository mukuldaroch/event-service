package com.daroch.event.services.impl;

import com.daroch.event.domain.entities.Event;
import com.daroch.event.domain.enums.EventStatusEnum;
import com.daroch.event.repositories.EventRepository;
import com.daroch.event.services.EventQueryService;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventQueryServiceImpl implements EventQueryService {

  private final EventRepository eventRepository;

  /**
   * Retrieves a single event belonging to a specific organizer.
   *
   * @param organizerId the UUID of the organizer; must not be null
   * @param eventId the UUID of the event to fetch; must not be null
   * @return an Optional containing the Event if found and owned by the organizer, otherwise empty
   */
  @Override
  public Optional<Event> getEventForOrganizer(UUID organizerId, UUID eventId) {
    return eventRepository.findByIdAndOrganizerId(eventId, organizerId);
  }

  /**
   * Retrieves a paginated list of events created by a specific organizer.
   *
   * @param organizerId the UUID of the organizer; must not be null
   * @param pageable pagination and sorting information
   * @return a Page of Event entities created by the organizer
   */
  @Override
  public Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable) {
    return eventRepository.findByOrganizerId(organizerId, pageable);
  }

  /**
   * Retrieves a paginated list of all events that are in PUBLISHED status.
   *
   * <p>This is used for publicly visible listings where only published events should be displayed.
   *
   * @param pageable pagination details such as page number and size
   * @return a paginated list of published events
   */
  @Override
  public Page<Event> listPublishedEvents(Pageable pageable) {
    return eventRepository.findByStatus(EventStatusEnum.PUBLISHED, pageable);
  }

  /**
   * Searches published events using a text-based query.
   *
   * <p>The search is delegated to the repository where full-text or LIKE-based search may be
   * implemented, and only events that are published are returned.
   *
   * @param query the search keyword to match against event fields
   * @param pageable pagination details for the result list
   * @return a paginated list of search results within published events
   */
  @Override
  public Page<Event> searchPublishedEvents(String query, Pageable pageable) {
    return eventRepository.searchEvents(query, pageable);
  }

  /**
   * Retrieves a published event by its ID.
   *
   * <p>This ensures that only events in PUBLISHED status are returned. If the event exists but is
   * not published, the result will be empty.
   *
   * @param eventId the UUID of the event to fetch
   * @return an Optional containing the published event if found, otherwise empty
   */
  @Override
  public Optional<Event> getPublishedEvent(UUID eventId) {
    return eventRepository.findByIdAndStatus(eventId, EventStatusEnum.PUBLISHED);
  }
}
