package com.daroch.event.controllers;

import com.daroch.event.domain.entities.Event;
import com.daroch.event.dto.response.PublishedEventResponse;
import com.daroch.event.mappers.EventMapper;
import com.daroch.event.services.EventQueryService;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class PublicEventController {

  private final EventQueryService eventQueryService;
  private final EventMapper eventMapper;

  /**
   * Retrieves a paginated list of publicly visible (published) events.
   *
   * <p>Pagination parameters are provided via query parameters: {@code page}, {@code size}, and
   * {@code sort}.
   *
   * @param pageable pagination and sorting information
   * @return a paginated list of published event responses
   */
  @GetMapping("/published")
  public Page<PublishedEventResponse> getPublicEvents(Pageable pageable) {
    Page<Event> events = eventQueryService.listPublishedEvents(pageable);
    Page<PublishedEventResponse> response = events.map(eventMapper::toPublishedEventResponseDto);

    return response;
  }

  /**
   * Retrieves a single published event by its ID.
   *
   * <p>If the event does not exist or is not published, an {@link EventNotFoundException} is
   * thrown.
   *
   * @param eventId unique identifier of the event
   * @return published event response
   */
  @GetMapping("/published/{eventId}")
  public PublishedEventResponse getPublicEvent(@PathVariable UUID eventId) {
    Event event = eventQueryService.getPublishedEvent(eventId);

    return eventMapper.toPublishedEventResponseDto(event);
  }
}
