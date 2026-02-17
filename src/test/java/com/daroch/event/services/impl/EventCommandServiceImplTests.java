package com.daroch.event.services.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.daroch.event.domain.entities.Event;
import com.daroch.event.domain.enums.EventStatusEnum;
import com.daroch.event.dto.commands.CreateEventCommand;
import com.daroch.event.dto.commands.UpdateEventCommand;
import com.daroch.event.exceptions.EventNotFoundException;
import com.daroch.event.exceptions.EventUpdateException;
import com.daroch.event.repositories.EventRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventCommandServiceImplTest {

  @Nested
  @DisplayName("Create Event")
  public class createEventTest {

    @Mock private EventRepository eventRepository;

    @InjectMocks private EventCommandServiceImpl eventCommandService;

    // Helper to create a reusable Event instance for tests
    private Event createEvent() {
      Event event = new Event();
      event.setOrganizerId(UUID.randomUUID());
      event.setName("Desi Laudai");
      event.setVenue("Naughty Ghaziabad");
      event.setStatus(EventStatusEnum.PUBLISHED);
      return event;
    }

    @Test
    @DisplayName("Should map request fields and persist event")
    void createEvent_success() {

      // Arrange: input command
      CreateEventCommand command = new CreateEventCommand();
      UUID organizerId = UUID.randomUUID();
      command.setName("Desi laudai");
      command.setVenue("Naughty Ghaziabad");

      // Arrange: repository save result (simulating DB-generated data)
      Event persistedEvent = createEvent();

      Mockito.when(eventRepository.save(Mockito.any(Event.class))).thenReturn(persistedEvent);

      // Act
      Event result = eventCommandService.createEvent(organizerId, command);

      // Assert: returned object reflects mapped input + generated ID
      assertAll(
          () -> Assertions.assertEquals("Desi laudai", result.getName()),
          () -> Assertions.assertEquals("Naughty Ghaziabad", result.getVenue()),
          () -> Assertions.assertEquals(organizerId, result.getOrganizerId()),
          () -> Assertions.assertEquals(persistedEvent.getEventId(), result.getEventId()));

      // Assert: verify what the service attempted to persist
      ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
      Mockito.verify(eventRepository).save(captor.capture());

      Event saved = captor.getValue();

      assertAll(
          () -> Assertions.assertEquals("Desi laudai", saved.getName()),
          () -> Assertions.assertEquals("Naughty Ghaziabad", saved.getVenue()),
          () -> Assertions.assertEquals(organizerId, saved.getOrganizerId()));
    }
  }

  @Nested
  @DisplayName("Update Event")
  class UpdateEventTests {

    @Mock private EventRepository eventRepository;

    @InjectMocks private EventCommandServiceImpl eventCommandService;

    @Test
    @DisplayName("Should throw exception when event ID is null")
    void updateEvent_nullEventId_throwsException() {

      // Arrange
      UpdateEventCommand cmd = new UpdateEventCommand();
      UUID organizerId = UUID.randomUUID();
      UUID eventId = null;

      // Act + Assert
      assertThrows(
          EventUpdateException.class,
          () -> eventCommandService.updateEventForOrganizer(organizerId, eventId, cmd));

      // Repository must not be touched on validation failure
      Mockito.verifyNoInteractions(eventRepository);
    }

    @Test
    @DisplayName("Should throw exception when event does not exist")
    void updateEvent_eventNotFound_throwsException() {

      // Arrange
      UUID organizerId = UUID.randomUUID();
      UUID eventId = UUID.randomUUID();

      UpdateEventCommand cmd = new UpdateEventCommand();

      Mockito.when(eventRepository.findByEventIdAndOrganizerId(eventId, organizerId))
          .thenReturn(Optional.empty());

      // Act + Assert
      assertThrows(
          EventNotFoundException.class,
          () -> eventCommandService.updateEventForOrganizer(organizerId, eventId, cmd));

      // Ensure no save attempt is made
      Mockito.verify(eventRepository).findByEventIdAndOrganizerId(eventId, organizerId);
      Mockito.verify(eventRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Should update only non-null fields")
    void updateEvent_partialUpdate_success() {

      // Arrange: existing persisted event
      UUID organizerId = UUID.randomUUID();
      UUID eventId = UUID.randomUUID();

      Event existingEvent = new Event();
      existingEvent.setEventId(eventId);
      existingEvent.setName("Old Name");
      existingEvent.setVenue("Old Venue");
      existingEvent.setStatus(EventStatusEnum.DRAFT);

      UpdateEventCommand cmd = new UpdateEventCommand();
      cmd.setName("New Name"); // should update
      cmd.setVenue(null); // should remain unchanged
      cmd.setStatus(EventStatusEnum.PUBLISHED);

      Mockito.when(eventRepository.findByEventIdAndOrganizerId(eventId, organizerId))
          .thenReturn(Optional.of(existingEvent));

      // Simulate JPA behavior: save returns the same mutated entity
      Mockito.when(eventRepository.save(Mockito.any(Event.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // Act
      Event result = eventCommandService.updateEventForOrganizer(organizerId, eventId, cmd);

      // Assert: returned entity reflects selective updates
      assertAll(
          () -> Assertions.assertEquals("New Name", result.getName()),
          () -> Assertions.assertEquals("Old Venue", result.getVenue()),
          () -> Assertions.assertEquals(EventStatusEnum.PUBLISHED, result.getStatus()));

      // Assert: persisted intent
      ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
      Mockito.verify(eventRepository).save(captor.capture());

      Event saved = captor.getValue();

      assertAll(
          () -> Assertions.assertEquals("New Name", saved.getName()),
          () -> Assertions.assertEquals("Old Venue", saved.getVenue()));
    }

    @Test
    @DisplayName("Should do nothing when event does not exist")
    void deleteEvent_eventNotFound_noAction() {

      // Arrange: identifiers and existing event
      UUID organizerId = UUID.randomUUID();
      UUID eventId = UUID.randomUUID();

      Event existingEvent = new Event();
      existingEvent.setEventId(eventId);
      existingEvent.setOrganizerId(organizerId);

      Mockito.when(eventRepository.findByEventIdAndOrganizerId(eventId, organizerId))
          .thenReturn(Optional.of(existingEvent));

      // Act
      eventCommandService.deleteEventForOrganizer(organizerId, eventId);

      // Assert: lookup and delete are performed
      Mockito.verify(eventRepository).findByEventIdAndOrganizerId(eventId, organizerId);
      Mockito.verify(eventRepository).delete(existingEvent);
    }

    @Test
    void deleteEventForOrganizer_whenEventDoesNotExist_shouldDoNothing() {

      // Arrange: no event found for given identifiers
      UUID organizerId = UUID.randomUUID();
      UUID eventId = UUID.randomUUID();

      Mockito.when(eventRepository.findByEventIdAndOrganizerId(eventId, organizerId))
          .thenReturn(Optional.empty());

      // Act
      eventCommandService.deleteEventForOrganizer(organizerId, eventId);

      // Assert: lookup happens but delete is not attempted
      Mockito.verify(eventRepository).findByEventIdAndOrganizerId(eventId, organizerId);
      Mockito.verify(eventRepository, Mockito.never()).delete(Mockito.any());
    }
  }
}
