package com.daroch.event.services.impl;

import com.daroch.event.domain.entities.Event;
import com.daroch.event.domain.enums.EventStatusEnum;
import com.daroch.event.dto.concrete.CachedEventPage;
import com.daroch.event.dto.response.EventResponse;
import com.daroch.event.exceptions.EventNotFoundException;
import com.daroch.event.mappers.EventMapper;
import com.daroch.event.repositories.EventRepository;
import com.daroch.event.services.EventQueryService;
import com.daroch.event.services.RedisService;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventQueryServiceImpl implements EventQueryService {

  private final EventRepository eventRepository;
  private final EventMapper eventMapper;
  private final RedisService redisService;

  /**
   * Retrieves a single event belonging to a specific organizer.
   *
   * @param organizerId the UUID of the organizer; must not be null
   * @param eventId the UUID of the event to fetch; must not be null
   * @return an Optional containing the Event if found and owned by the organizer, otherwise empty
   */
  @Override
  public EventResponse getEventForOrganizer(UUID organizerId, UUID eventId) {

    Event event =
        eventRepository
            .findByEventIdAndOrganizerId(eventId, organizerId)
            .orElseThrow(EventNotFoundException::new);

    EventResponse eventResponse = eventMapper.toEventResponseDto(event);

    return eventResponse;
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
    // cache key
    String key =
        "public_published_events:"
            + "page="
            + pageable.getPageNumber()
            + ":size="
            + pageable.getPageSize()
            + ":sort="
            + pageable.getSort();

    // Try fetching cached page data
    Optional<CachedEventPage> cached = redisService.get(key, CachedEventPage.class);

    // Cache hit → reconstruct Spring Page and return
    if (cached.isPresent()) {
      CachedEventPage cachedPage = cached.get();

      int pageSize = cachedPage.getPageSize();
      if (pageSize < 1) {
        // cache must never break pagination
        pageSize = pageable.getPageSize();
      }

      return new PageImpl<>(
          cachedPage.getContent(),
          PageRequest.of(cachedPage.getPageNumber(), pageSize, pageable.getSort()),
          cachedPage.getTotalElements());
    }

    // Cache miss → fetch from database
    Page<Event> events = eventRepository.findByStatus(EventStatusEnum.PUBLISHED, pageable);

    // Convert Page → cache-friendly DTO
    CachedEventPage cacheable = new CachedEventPage();
    cacheable.setContent(events.getContent());
    cacheable.setPageNumber(events.getNumber());
    cacheable.setPageSize(events.getSize());
    cacheable.setTotalElements(events.getTotalElements());

    // Store DTO in Redis
    redisService.set(key, cacheable, Duration.ofMinutes(10));

    return events;
  }

  /**
   * Retrieves a published event by its unique identifier.
   *
   * <p>This method enforces public visibility rules by returning only events that are in {@link
   * EventStatusEnum#PUBLISHED} state.
   *
   * <p>If no published event exists with the given ID, a {@link EventNotFoundException} is thrown.
   *
   * @param eventId unique identifier of the event to retrieve
   * @return the published {@link Event}
   * @throws EventNotFoundException if the event does not exist or is not published
   */
  @Override
  @Cacheable(value = "public-published-event", key = "#eventId")
  public Event getPublishedEvent(UUID eventId) {

    String key = "public_published_event_" + eventId;

    // get the cached value
    Optional<Event> cached = redisService.get(key, Event.class);

    // if key is present
    if (cached.isPresent()) {
      return cached.get();
    }

    // if key is not present
    Event event =
        eventRepository
            .findByEventIdAndStatus(eventId, EventStatusEnum.PUBLISHED)
            .orElseThrow(() -> new EventNotFoundException());

    // set the key in redis
    redisService.set(key, event, Duration.ofMinutes(10));

    return event;
  }

  // TODO: implement serch throught the published events
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
  // @Override
  // public Page<Event> searchPublishedEvents(String query, Pageable pageable)
  // {
  //   return eventRepository.searchEvents(query, pageable);
  // }
}
