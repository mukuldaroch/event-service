package com.daroch.event.services.impl;

import static org.junit.jupiter.api.Assertions.assertAll;

import com.daroch.event.domain.entities.Event;
import com.daroch.event.dto.commands.CreateEventCommand;
import com.daroch.event.dto.commands.UpdateEventCommand;
import com.daroch.event.repositories.EventRepository;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@ExtendWith(MockitoExtension.class)
class EventCommandServiceImplTest {

  @Mock private EventRepository eventRepository;

  @InjectMocks private EventCommandServiceImpl eventCommandService;

  @Test
  void createEvent_whenValidCommand_shouldSaveMappedEvent() {

    // Arrange
    UUID organizerId = UUID.randomUUID();

    CreateEventCommand command = new CreateEventCommand();
    command.setName("Desi laudai");
    command.setVenue("Naughty Ghaziabad");

    Event persistedEvent = new Event();
    persistedEvent.setEventId(UUID.randomUUID());

    Mockito.when(eventRepository.save(Mockito.any(Event.class))).thenReturn(persistedEvent);

    // Act
    Event result = eventCommandService.createEvent(organizerId, command);

    // Assert (returned object)
    assertAll(
        () -> Assertions.assertEquals("Desi laudai", result.getName()),
        () -> Assertions.assertEquals("Naughty Ghaziabad", result.getVenue()),
        () -> Assertions.assertEquals(organizerId, result.getOrganizerId()),
        () -> Assertions.assertEquals(persistedEvent.getEventId(), result.getEventId()));

    // Assert (interaction + intent)
    ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
    Mockito.verify(eventRepository).save(captor.capture());

    Event saved = captor.getValue();

    assertAll(
        () -> Assertions.assertEquals("Desi laudai", saved.getName()),
        () -> Assertions.assertEquals("Naughty Ghaziabad", saved.getVenue()),
        () -> Assertions.assertEquals(organizerId, saved.getOrganizerId()));
  }

  @Test
  void updateEvent_whenValidCommand_shouldUpdateMappedEvent() {
    // Arrange
    UUID organizerId = UUID.randomUUID();

    UpdateEventCommand command = new UpdateEventCommand();

    command.setName("Pardesi laudai");
    command.setVenue("Naughty America");

    Event persistedEvent = new Event();
    persistedEvent.setEventId(UUID.randomUUID());

  }
}

/*
// * I tested the service’s contract.
// * I verifed the persisted entity using an ArgumentCaptor and only assert
fields owned by this service.
// * I avoid Spring context because this is a pure unit test.
*/
