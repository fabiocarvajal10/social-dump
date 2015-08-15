package com.coredump.socialdump.web.rest.mapper;

import com.coredump.socialdump.domain.*;
import com.coredump.socialdump.web.rest.dto.EventStatusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity EventStatus and its DTO EventStatusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EventStatusMapper {

    EventStatusDTO eventStatusToEventStatusDTO(EventStatus eventStatus);

    @Mapping(target = "eventsById", ignore = true)
    EventStatus eventStatusDTOToEventStatus(EventStatusDTO eventStatusDTO);
}
