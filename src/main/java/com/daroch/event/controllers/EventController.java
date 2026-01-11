package com.daroch.event.controllers;

import com.daroch.event.domain.entities.Event;
import com.daroch.event.dto.request.CreateEventRequest;
import com.daroch.event.dto.response.CreateEventResponse;
import com.daroch.event.dto.response.EventResponse;
import com.daroch.event.mappers.EventMapper;
import com.daroch.event.services.EventCommandService;
import com.daroch.event.services.EventQueryService;
import com.daroch.event.services.commands.CreateEventCommand;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventController {

  private final EventMapper eventMapper;
  private final EventQueryService eventQueryService;
  private final EventCommandService eventCommandService;

  /**
   * Extracts the user (organizer) ID from the JWT token. The JWT's subject is assumed to store the
   * user's UUID.
   */
  private UUID parseUserId(Jwt jwt) {
    return UUID.fromString(jwt.getSubject());
  }

  /**
   * POST /events Creates a new event for the authenticated organizer.
   *
   * @param jwt JWT token containing the user ID
   * @param createEventRequestDto incoming event creation payload
   * @return 201 Created with event details
   */
  @PostMapping
  public ResponseEntity<CreateEventResponse> createEvent(
      @AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateEventRequest EventRequest) {

    // Convert incoming DTO → internal model used by service
    CreateEventCommand createEventCommand = eventMapper.fromCreateEventRequestDto(EventRequest);

    // Extract organizer/user ID from JWT
    UUID userId = parseUserId(jwt);

    // Delegate the actual creation logic to the service
    Event createdEvent = eventCommandService.createEvent(userId, createEventCommand);

    // Convert the saved entity → response DTO for API response
    CreateEventResponse createEventResponse = eventMapper.toCreateEventResponseDto(createdEvent);

    // Return 201 CREATED with the new event details
    return new ResponseEntity<>(createEventResponse, HttpStatus.CREATED);
  }

  /**
   * GET /events Lists all events belonging to the authenticated organizer. Supports pagination via
   * Spring's Pageable.
   *
   * @param jwt JWT token containing user ID
   * @param pageable Spring’s pagination and sorting abstraction
   * @return 200 OK with paginated list of events
   */
  // @GetMapping
  // public ResponseEntity<Page<EventResponse>> listEvents(
  //     @AuthenticationPrincipal Jwt jwt, Pageable pageable) {
  //
  //   // Extract organizer ID from JWT
  //   UUID userId = parseUserId(jwt);
  //
  //   // Fetch paginated events for this organizer
  //   Page<Event> events = eventQueryService.listEventsForOrganizer(userId,
  //   pageable);
  //
  //   // Map entity page → DTO page
  //   return
  //   ResponseEntity.ok(events.map(eventMapper::tolistEventResponseDto));
  // }

  /**
   * GET /events/{eventId} Retrieves details of a specific event if it belongs to the authenticated
   * organizer.
   *
   * @param jwt JWT token containing organizer ID
   * @param eventId ID of the event to fetch
   * @return 200 OK with event details or 404 if not found
   */
  @GetMapping("/{eventId}")
  public ResponseEntity<EventResponse> getEvent(
      @AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId) {

    UUID organizerId = parseUserId(jwt);

    // Find the event for the given organizer
    return eventQueryService
        .getEventForOrganizer(organizerId, eventId)
        .map(eventMapper::toEventResponseDto)
        .map(ResponseEntity::ok) // wrap in 200 OK
        .orElse(ResponseEntity.notFound().build()); // else return 404

    // .build() means no body, only status + headers
  }

  // /**
  //  * POST /events/{eventId} Updates an existing event and its ticket types.
  //  *
  //  * @param jwt JWT token containing user ID
  //  * @param eventId ID of the event to update
  //  * @param updateEventRequestDto payload with updated fields
  //  * @return 200 OK with updated event details
  //  */
  // @PutMapping("/{eventId}")
  // public ResponseEntity<UpdateEventResponseDto> updateEvent(
  //     @AuthenticationPrincipal Jwt jwt,
  //     @PathVariable UUID eventId,
  //     @Valid @RequestBody UpdateEventRequestDto updateEventRequestDto) {
  //
  //   // Convert DTO → domain model for service layer
  //   UpdateEventRequest updateEventRequest =
  //       eventMapper.fromUpdateEventRequestDto(updateEventRequestDto);
  //
  //   // Extract user ID from JWT
  //   UUID userId = parseUserId(jwt);
  //
  //   // Delegate update logic to the service
  //   Event updatedEvent =
  //       eventCommandService.updateEventForOrganizer(userId, eventId,
  //       updateEventRequest);
  //
  //   // Convert the updated entity → response DTO
  //   UpdateEventResponseDto updateEventResponseDto =
  //       eventMapper.toUpdateEventResponseDto(updatedEvent);
  //
  //   // Return 200 OK with updated data
  //   return ResponseEntity.ok(updateEventResponseDto);
  // }
  //
  // @DeleteMapping(path = "/{eventId}")
  // public ResponseEntity<Void> deleteEvent(
  //     @AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId) {
  //   UUID userId = parseUserId(jwt);
  //   eventCommandService.deleteEventForOrganizer(userId, eventId);
  //   return ResponseEntity.noContent().build();
  // }
}
