package com.daroch.event.services.impl;

import com.daroch.event.repositories.EventRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EventCommandServiceImplTests {
  @Mock EventRepository eventRepository;

  @Test
  void createEventShouldCreateEvent() {}
}
