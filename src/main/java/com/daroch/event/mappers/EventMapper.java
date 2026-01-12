package com.daroch.event.mappers;

import com.daroch.event.domain.entities.Event;
import com.daroch.event.dto.request.CreateEventRequest;
import com.daroch.event.dto.request.UpdateEventRequest;
import com.daroch.event.dto.response.CreateEventResponse;
import com.daroch.event.dto.response.EventResponse;
import com.daroch.event.services.commands.CreateEventCommand;
import com.daroch.event.services.commands.UpdateEventCommand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

  /**
   * converts CreateEventRequest Dto to CreateEventCommand Dto
   *
   * @param createEventRequest Dto incoming event creation payload
   * @return CreateEventCommand
   */
  CreateEventCommand fromCreateEventRequestDto(CreateEventRequest dto);

  /**
   * converts Event Object to CreateEventResponse Dto
   *
   * @param created Event payload
   * @return CreateEventResponse Dto
   */
  CreateEventResponse toCreateEventResponseDto(Event event);

  /**
   * converts Event Object to EventResponse Dto
   *
   * @param createEventRequestDto incoming event creation payload
   * @return CreateEventResponse Dto
   */
  EventResponse toEventResponseDto(Event event);

  /**
   * converts UpdateEventRequest Dto to UpdateEventCommand Dto
   *
   * @param createEventRequest Dto incoming event creation payload
   * @return UpdateEventCommand
   */
  UpdateEventCommand fromUpdateEventRequestDto(UpdateEventRequest dto);
}
