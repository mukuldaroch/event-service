package com.daroch.event.repositories;

import com.daroch.event.domain.entities.Event;
import com.daroch.event.domain.enums.EventStatusEnum;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Transactional
@Rollback
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EventRepositoryTest {

  // Injects the real JPA repository backed by H2 (not mocks)
  @Autowired private EventRepository eventRepository;

  // This will be reused in every test
  private Event event;

  /** Runs BEFORE EACH @Test method Creates a fresh Event object so tests remain isolated */
  @BeforeEach
  void setup() {
    event = new Event();

    // Setting required fields
    event.setOrganizerId(UUID.randomUUID());
    event.setName("Desi Laudai");
    event.setVenue("Naughty Ghaziabad");
    event.setStatus(EventStatusEnum.DRAFT);

    // Using very old dates to avoid "future date" constraints if any exist
    event.setEventStartDate(LocalDateTime.of(1000, 1, 1, 1, 1));
    event.setEventEndDate(LocalDateTime.of(1000, 1, 1, 1, 1));
  }

  @AfterEach
  void cleanup() {
    eventRepository.deleteAll();
  }

  /**
   * Verifies that saving an Event persists it and auto-populated fields (like createdAt) are set
   */
  @Test
  void EventRepository_save_ReturnSavedEvents() {

    // Act: save entity and force DB write immediately
    Event savedEvent = eventRepository.saveAndFlush(event);

    // Assert: entity exists and auditing fields are populated
    Assertions.assertThat(savedEvent).isNotNull();
    Assertions.assertThat(savedEvent.getCreatedAt()).isNotNull();
  }

  /** Verifies custom finder method: findByOrganizerId */
  @Test
  void EventRepository_findByOrganizerId_ReturnSavedEvents() {

    // Arrange
    Event savedEvent = eventRepository.save(event);
    eventRepository.flush(); // ensures SQL execution

    // Act
    Pageable pageable = PageRequest.of(0, 10);
    Page<Event> fetchedEvents = eventRepository.findByOrganizerId(event.getOrganizerId(), pageable);

    // Assert
    Assertions.assertThat(fetchedEvents.getContent()).hasSize(1);
    Assertions.assertThat(fetchedEvents.getContent().get(0).getOrganizerId())
        .isEqualTo(savedEvent.getOrganizerId());
  }

  /** Verifies custom finder method: findByStatus */
  @Test
  void EventRepository_findByStatus_ReturnSavedEvents() {

    // Arrange
    Event savedEvent = eventRepository.save(event);
    eventRepository.flush();

    // Act
    Pageable pageable = PageRequest.of(0, 10);
    Page<Event> fetchedEvents = eventRepository.findByStatus(EventStatusEnum.DRAFT, pageable);

    // Assert
    Assertions.assertThat(fetchedEvents.getContent()).hasSize(1);

    Assertions.assertThat(fetchedEvents.getContent().get(0).getStatus())
        .isEqualTo(savedEvent.getStatus());
  }

  /** Verifies custom finder method: findByStatus */
  @Test
  void EventRepository_findByEventIdAndStatus_ReturnSavedEvents() {
    // Arrange
    Event savedEvent = eventRepository.save(event);
    eventRepository.flush();

    // Act
    Optional<Event> fetchedEvent =
        eventRepository.findByEventIdAndStatus(savedEvent.getEventId(), savedEvent.getStatus());

    // Assert
    Assertions.assertThat(fetchedEvent).isPresent();
  }
}
