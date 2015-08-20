package com.coredump.socialdump.web.rest.mapper;

import com.coredump.socialdump.domain.EventType;
import com.coredump.socialdump.web.rest.dto.EventTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity EventType and its DTO EventTypeDTO.
 *
 * @author Esteban
 */
@Mapper(componentModel = "spring", uses = {})
public interface EventTypeMapper {

  /**
   * Traduce objetos de dominio a objetos limpios para
   * ser utilizados para retornarlos por el API.
   *
   * @param eventType estado de evento.
   * @return objeto de transmision de datos.
   */
  EventTypeDTO eventTypeToEventTypeDTO(EventType eventType);

  /**
   * Traduce objetos de transmisión de datos a objetos de dominio.
   *
   * @param eventTypeDTO objeto de transmisión de datos.
   * @return objeto de dominio.
   */
  @Mapping(target = "eventsById", ignore = true)
  EventType eventTypeDTOToEventType(EventTypeDTO eventTypeDTO);
}
