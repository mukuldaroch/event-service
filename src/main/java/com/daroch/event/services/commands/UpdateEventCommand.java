package com.daroch.event.services.commands;

import com.daroch.event.domain.enums.EventStatusEnum;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventCommand {

  private UUID eventId;
  private String name;
  private String venue;
  private EventStatusEnum status;

  private LocalDateTime eventStartDate;
  private LocalDateTime eventEndDate;
  private LocalDateTime salesStartDate;
  private LocalDateTime salesEndDate;
}
