package com.daroch.event.services.impl;

import com.daroch.event.domain.entities.Event;
import com.daroch.event.dto.commands.CreateEventCommand;
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
    Assertions.assertEquals("Desi laudai", result.getName());
    Assertions.assertEquals("Naughty Ghaziabad", result.getVenue());
    Assertions.assertEquals(organizerId, result.getOrganizerId());
    Assertions.assertEquals(persistedEvent.getEventId(), result.getEventId());

    // Assert (interaction + intent)
    ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
    Mockito.verify(eventRepository).save(captor.capture());

    Event saved = captor.getValue();
    Assertions.assertEquals("Desi laudai", saved.getName());
    Assertions.assertEquals("Naughty Ghaziabad", saved.getVenue());
    Assertions.assertEquals(organizerId, saved.getOrganizerId());
  }
}

/*
// * I tested the service’s contract.
// * I verifed the persisted entity using an ArgumentCaptor and only assert
fields owned by this service.
// * I avoid Spring context because this is a pure unit test.
*/
