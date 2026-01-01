package com.daroch.event.mappers;

import com.daroch.event.domain.entities.Event;
import com.daroch.event.dtos.request.CreateEventRequest;
import com.daroch.event.dtos.response.CreateEventResponse;
import com.daroch.event.services.commands.CreateEventCommand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

  /**
   * converts CreateEventRequest Dto to CreateEventCommand Dto
   *
   * @param createEventRequestDto incoming event creation payload
   * @return CreateEventCommand
   */
  CreateEventCommand fromDto(CreateEventRequest dto);

  /**
   * converts Event Object to CreateEventResponse Dto 
   *
   * @param createEventRequestDto incoming event creation payload
   * @return CreateEventResponse Dto
   */
  CreateEventResponse toDto(Event event);
}
