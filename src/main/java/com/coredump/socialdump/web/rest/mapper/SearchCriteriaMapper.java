package com.coredump.socialdump.web.rest.mapper;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.GenericStatus;
import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.domain.SocialNetwork;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.repository.GenericStatusRepository;
import com.coredump.socialdump.repository.SocialNetworkRepository;
import com.coredump.socialdump.web.rest.dto.SearchCriteriaDTO;
import com.coredump.socialdump.web.rest.dto.SearchCriteriaRequestDTO;
import javax.inject.Inject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Created by fabio on 7/19/15.
 * Mapper for the entity SearchCriteria and its DTO SearchCriteriaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SearchCriteriaMapper {

  // @Inject
  // private EventRepository eventRepository;

  // @Inject
  // private SocialNetworkRepository socialNetworkRepository;

  // @Inject
  // private GenericStatusRepository genericStatusRepository;

  @Mappings({
    @Mapping(source = "eventByEventId.id",
             target = "eventId"),
    @Mapping(source = "eventByEventId.description",
             target = "eventDescription"),
    @Mapping(source = "socialNetworkBySocialNetworkId.id",
             target = "socialNetworkId"),
    @Mapping(source = "socialNetworkBySocialNetworkId.name",
             target = "socialNetworkName"),
    @Mapping(source = "genericStatusByStatusId.id",
             target = "statusId"),
    @Mapping(source = "genericStatusByStatusId.status",
             target = "status")
  })
  SearchCriteriaDTO searchCriteriaToSearchCriteriaDTO(
    SearchCriteria searchCriteria);

  /**
   * Obtiene un criterio de búsqueda de un DTO
   * @param searchCriteriaDTO DTO de criterio de búsqueda
   * @return criterio de búsqueda
   */
  @Mappings({
    @Mapping(source = "eventId",
             target = "eventByEventId"),
    @Mapping(target = "socialNetworkBySocialNetworkId",
             source = "socialNetworkId"),
    @Mapping(target = "genericStatusByStatusId",
             source = "statusId"),
    @Mapping(target = "socialNetworkPostsById",
             ignore = true)
  })
  SearchCriteria searchCriteriaDTOToSearchCriteria(
    SearchCriteriaDTO searchCriteriaDTO);

  @Mappings({
        @Mapping(source = "socialNetworkId", target = "socialNetworkBySocialNetworkId"),
        @Mapping(source = "eventId", target = "eventByEventId"),
        @Mapping(source = "genericStatusId", target = "genericStatusByStatusId")})
  SearchCriteria searchCriteriaRequestDTOToSearchCriteria(
        SearchCriteriaRequestDTO searchCriteriaRequestDTO);

  /**
   * Genera un evento de un Id
   * @param id id del evento
   * @return evento
   */
  default Event eventFromId(Long id) {
    if (id == null) {
      return null;
    }
    Event event = new Event();
    event.setId(id);
    return event;
  }

  /**
   * Genera una red social a partir de un id
   * @param id id de la red social
   * @return red social
   */
  default SocialNetwork socialNetworkFromId(Integer id) {
    if (id == null) {
      return null;
    }
    SocialNetwork socialNetwork = new SocialNetwork();
    socialNetwork.setId(id);
    return socialNetwork;
  }

  /**
   * Genera un estado a partir de un id
   * @param id id del estado
   * @return estado
   */
  default GenericStatus genericStatusFromId(Short id) {
    if (id == null) {
      return null;
    }
    GenericStatus genericStatus = new GenericStatus();
    genericStatus.setId(id);
    return genericStatus;
  }

}
