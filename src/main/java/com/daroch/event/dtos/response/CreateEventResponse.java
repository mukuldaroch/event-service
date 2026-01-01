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
public class CreateEventResponse {

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
// ---------------------------------------------------------------------
// This is the data you send back to the client after the API call.
// ---------------------------------------------------------------------

// {
// "id": "123e4567-e89b-12d3-a456-426614174000",
// "name": "Metal Concert",
// "start": "2025-12-31T20:00",
// "end": "2025-12-31T23:00",
// "venue": "Stadium",
// "status": "UPCOMING",
// "ticketTypes": [
// { "id": "aaa111", "name": "VIP", "price": 5000 },
// { "id": "bbb222", "name": "Regular", "price": 1000 }
// ]
// }
