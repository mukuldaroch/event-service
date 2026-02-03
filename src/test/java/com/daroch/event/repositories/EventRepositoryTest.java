package com.daroch.event.repositories;

import com.daroch.event.domain.entities.Event;
import com.daroch.event.domain.enums.EventStatusEnum;
import java.time.LocalDateTime;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EventRepositoryTest {
  @Autowired private EventRepository eventRepository;

  @Test
  public void EventRepository_save_ReturnSavedEvents() {
    // Arrange
    var event = new Event();

    event.setOrganizerId(UUID.randomUUID());
    event.setName("Desi Laudai");
    event.setVenue("Naughty Ghaziabad");
    event.setStatus(EventStatusEnum.DRAFT);
    event.setEventStartDate(LocalDateTime.of(1000, 1, 1, 1, 1));
    event.setEventEndDate(LocalDateTime.of(1000, 1, 1, 1, 1));

    // Act
    Event savedEvent = eventRepository.saveAndFlush(event);
    // Assert
    Assertions.assertThat(savedEvent).isNotNull();
    Assertions.assertThat(savedEvent.getCreatedAt()).isNotNull();
  }

  @Test
  public void EventRepository_findByOrganizerId_ReturnSavedEvents() {

    // Arrange
    var event = new Event();
    event.setOrganizerId(UUID.randomUUID());
    event.setName("Desi Laudai");
    event.setVenue("Naughty Ghaziabad");
    event.setStatus(EventStatusEnum.DRAFT);
    event.setEventStartDate(LocalDateTime.of(1000, 1, 1, 1, 1));
    event.setEventEndDate(LocalDateTime.of(1000, 1, 1, 1, 1));

    // Act
    Pageable pageable = PageRequest.of(0, 10);
    Event savedEvent = eventRepository.save(event);
    eventRepository.flush();

    Page<Event> fetchedEvents = eventRepository.findByOrganizerId(event.getOrganizerId(), pageable);

    // Assert
    Assertions.assertThat(fetchedEvents.getContent()).hasSize(1);
    Assertions.assertThat(fetchedEvents.getContent().get(0).getOrganizerId())
        .isEqualTo(savedEvent.getOrganizerId());
  }

  @Test
  public void EventRepository_findByStatus_ReturnSavedEvents() {
    // Arrange
    var event = new Event();
    event.setOrganizerId(UUID.randomUUID());
    event.setName("Desi Laudai");
    event.setVenue("Naughty Ghaziabad");
    event.setStatus(EventStatusEnum.DRAFT);
    event.setEventStartDate(LocalDateTime.of(1000, 1, 1, 1, 1));
    event.setEventEndDate(LocalDateTime.of(1000, 1, 1, 1, 1));
    // Act

    Event SavedEvent = eventRepository.save(event);
    eventRepository.flush();

    // Assert
    Pageable pageable = PageRequest.of(0, 10);
    Page<Event> fetchedEvents = eventRepository.findByStatus(EventStatusEnum.DRAFT, pageable);
    Assertions.assertThat(fetchedEvents.getContent().get(0).getStatus())
        .isEqualTo(SavedEvent.getStatus());
  }
}
