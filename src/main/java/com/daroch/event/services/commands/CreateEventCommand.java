package com.daroch.event.services.commands;

import com.daroch.event.domain.enums.EventStatusEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventCommand {

  private String name;
  private LocalDateTime start;
  private LocalDateTime end;
  private String venue;
  private LocalDateTime salesStartDate;
  private LocalDateTime salesEndDate;
  private EventStatusEnum status;
}
// POST /events
// {
//     "name": "Music Concert",
//     "start": "2025-12-01T18:00:00",
//     "end": "2025-12-01T21:00:00",
//     "venue": "City Hall",
//     "salesStart": "2025-11-01T00:00:00",
//     "salesEnd": "2025-11-30T23:59:00",
//     "status": "ACTIVE",
//     "organiserId": 5,
//     "ticketTypes": [
//         {
//             "name": "VIP",
//             "price": 150.0,
//             "description": "Best seats",
//             "totalAvailable": 50
//         },
//         {
//             "name": "Regular",
//             "price": 50.0,
//             "description": "Standard seat",
//             "totalAvailable": 200
//         }
//     ]
// }
