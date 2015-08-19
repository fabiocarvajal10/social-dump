package com.coredump.socialdump.web.rest.mapper;

import com.coredump.socialdump.domain.*;
import com.coredump.socialdump.web.rest.dto.EventTypeDTO;

import org.mapstruct.*;


/**
 * Mapper for the entity EventType and its DTO EventTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EventTypeMapper {
  EventTypeDTO eventTypeToEventTypeDTO(EventType eventType);

  @Mapping(target = "eventsById", ignore = true)
  EventType eventTypeDTOToEventType(EventTypeDTO eventTypeDTO);
}
