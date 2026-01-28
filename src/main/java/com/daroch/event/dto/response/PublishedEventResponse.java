package com.daroch.event.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PublishedEventResponse {

  private UUID eventId;
  private String name;
  private String venue;

  private LocalDateTime eventStartDate;
  private LocalDateTime eventEndDate;
}
