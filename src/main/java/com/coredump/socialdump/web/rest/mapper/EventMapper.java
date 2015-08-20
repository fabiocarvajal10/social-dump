package com.coredump.socialdump.web.rest.mapper;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.EventStatus;
import com.coredump.socialdump.domain.EventType;
import com.coredump.socialdump.domain.Organization;
import com.coredump.socialdump.repository.EventStatusRepository;
import com.coredump.socialdump.repository.EventTypeRepository;
import com.coredump.socialdump.repository.OrganizationRepository;
import com.coredump.socialdump.web.rest.dto.EventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import javax.inject.Inject;

/**
 * Created by fabio on 13/07/15.
 */

@Mapper(componentModel = "spring", uses = {})
public abstract class EventMapper {

  @Inject
  private EventStatusRepository eventStatusRepository;

  @Inject
  private EventTypeRepository eventTypeRepository;

  @Inject
  private OrganizationRepository organizationRepository;

  @Mappings({
    @Mapping(source = "organizationByOrganizationId.id", target = "organizationId"),
    @Mapping(source = "eventStatusByStatusId.id", target = "statusId"),
    @Mapping(source = "eventTypeByEventTypeId.id", target = "typeId")})
  public abstract EventDTO eventToEventDTO(Event event);

  @Mappings({
    @Mapping(source = "organizationId", target = "organizationByOrganizationId"),
    @Mapping(source = "statusId", target = "eventStatusByStatusId"),
    @Mapping(source = "typeId", target = "eventTypeByEventTypeId")})
  public abstract Event eventDTOToEvent(EventDTO eventDTO);

  /**
   * Obtiene un eventStatus por un id.
   * @param id id del eventStatus
   * @return EventStatus
   */
  public EventStatus eventStatusFromId(Short id) {
    if (id == null) {
      return null;
    }
    return eventStatusRepository.findOne(id);
  }

  /**
   * Obtiene una organization por un id.
   * @param id id del organization
   * @return Organization
   */
  public Organization organizationFromId(Long id) {
    if (id == null) {
      return null;
    }
    return organizationRepository.findOne(id);
  }

  /**
   * Obtiene un EventType por un id.
   * @param id id del EventType
   * @return EventType
   */
  public EventType eventTypeFromId(Integer id) {
    if (id == null) {
      return null;
    }
    return eventTypeRepository.findOne(id);
  }

}
