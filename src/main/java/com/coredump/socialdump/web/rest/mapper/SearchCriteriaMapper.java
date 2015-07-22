package com.coredump.socialdump.web.rest.mapper;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.GenericStatus;
import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.domain.SocialNetwork;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.repository.GenericStatusRepository;
import com.coredump.socialdump.repository.SocialNetworkRepository;
import com.coredump.socialdump.web.rest.dto.SearchCriteriaRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import javax.inject.Inject;

/**
 * Created by fabio on 7/19/15.
 * Mapper for the entity SearchCriteria and its DTO SearchCriteriaDTO.
 */

@Mapper(componentModel = "spring", uses = {})
public abstract class SearchCriteriaMapper {

  @Inject
  private EventRepository eventRepository;

  @Inject
  private SocialNetworkRepository socialNetworkRepository;

  @Inject
  private GenericStatusRepository genericStatusRepository;

  @Mappings({
        @Mapping(source = "socialNetworkId", target = "socialNetworkBySocialNetworkId"),
        @Mapping(source = "eventId", target = "eventByEventId"),
        @Mapping(source = "genericStatusId", target = "genericStatusByStatusId")})
  public abstract SearchCriteria searchCriteriaRequestDTOToSearchCriteria(
        SearchCriteriaRequestDTO searchCriteriaRequestDTO);

  /**
   *
   */
  public Event eventFromId(Long id) {
    if (id == null) {
      return null;
    }
    return eventRepository.findOne(id);
  }

  /**
   *
   */
  public SocialNetwork socialNetworkFromID(int id) {
    if (id < 1) {
      return null;
    }
    return socialNetworkRepository.findOne(id);
  }

  /**
   *
   */
  public GenericStatus genericStatusFromId(short id) {
    if (id < 1) {
      return null;
    }
    return genericStatusRepository.findOne(id);
  }
}
