package com.daroch.event.dto.request;

import com.daroch.event.domain.enums.EventStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {

  @NotBlank(message = "Event name is required")
  private String name;

  @NotBlank(message = "venue information are required")
  private String venue;

  @NotNull(message = "Event status is required")
  private EventStatusEnum status;


  private LocalDateTime eventStartDate;
  private LocalDateTime eventEndDate;
  private LocalDateTime salesStartDate;
  private LocalDateTime salesEndDate;
}
