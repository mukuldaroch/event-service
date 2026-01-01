package com.daroch.event.dtos.response;

import com.daroch.event.domain.enums.EventStatusEnum;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventResponse {

  private UUID id;
  private String name;
  private String description;
  private LocalDateTime start;
  private LocalDateTime end;
  private String venue;
  private LocalDateTime salesStartDate;
  private LocalDateTime salesEndDate;
  private EventStatusEnum status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
