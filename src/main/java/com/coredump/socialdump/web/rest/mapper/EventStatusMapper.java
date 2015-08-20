package com.coredump.socialdump.web.rest.mapper;

import com.coredump.socialdump.domain.EventStatus;
import com.coredump.socialdump.web.rest.dto.EventStatusDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity EventStatus and its DTO EventStatusDTO.
 * Traductor de objetos de dominio a objetos de transmision de datos y
 * viceversa.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EventStatusMapper {

  /**
   * Traduce objetos de dominio (estados de evento) a objetos limpios para
   * ser utilizados para retornarlos por el API.
   *
   * @param eventStatus estado de evento.
   * @return objeto de transmision de datos.
   */
  EventStatusDTO eventStatusToEventStatusDTO(EventStatus eventStatus);

  /**
   * Traduce objetos de transmisión de datos a objetos de dominio.
   *
   * @param eventStatusDTO objeto de transmisión de datos.
   * @return objeto de dominio.
   */
  @Mapping(target = "eventsById", ignore = true)
  EventStatus eventStatusDTOToEventStatus(EventStatusDTO eventStatusDTO);
}
