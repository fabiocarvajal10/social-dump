package com.coredump.socialdump.web.rest.mapper;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.GenericStatus;
import com.coredump.socialdump.domain.MonitorContact;
import com.coredump.socialdump.domain.TemporalAccess;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.repository.GenericStatusRepository;
import com.coredump.socialdump.repository.MonitorContactRepository;
import com.coredump.socialdump.web.rest.dto.TemporalAccessDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import javax.inject.Inject;

/**
 * Created by Franz on 22/07/2015.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class TemporalAccessMapper {

  @Inject
  private MonitorContactRepository monitorContactRepository;

  @Inject
  private EventRepository eventRepository;

  @Inject
  private GenericStatusRepository genericStatusRepository;

  @Mappings({
      @Mapping(source = "eventId", target = "eventByEventId"),
      @Mapping(source = "monitorContactId", target = "monitorContactByMonitorContactId")})
  public abstract TemporalAccess temporalAccessDTOToTemporalAccess(TemporalAccessDTO temporalAccessDTO);

  /**
   * Obtiene una Event por un id.
   * @param id id del Event
   * @return Event
   */
  public Event eventFromId(Long id) {
    if (id == null) {
      return null;
    }

    return eventRepository.findOne(id);
  }

  /**
   * Obtiene una MonitorContact por un id.
   * @param id id del MonitorContact
   * @return MonitorContact
   */
  public MonitorContact monitorContactFromId(Long id) {
    if (id == null) {
      return null;
    }

    return monitorContactRepository.findOne(id);
  }

  /**
   * Obtiene una GenericStatus por un id.
   * @param id id del GenericStatus
   * @return GenericStatus
   */
  public GenericStatus genericStatusFromId(Short id) {
    if (id == null) {
      return null;
    }

    return genericStatusRepository.findOne(id);
  }
}
