package com.coredump.socialdump.web.rest.mapper;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.domain.SocialNetwork;
import com.coredump.socialdump.domain.SocialNetworkPost;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.repository.SearchCriteriaRepository;
import com.coredump.socialdump.repository.SocialNetworkRepository;
import com.coredump.socialdump.web.rest.dto.SocialNetworkPostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import javax.inject.Inject;

/**
 * Created by fabio on 7/25/15.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class SocialNetworkPostMapper {
  @Inject
  private SocialNetworkRepository socialNetworkRepository;

  @Inject
  private SearchCriteriaRepository searchCriteriaRepository;

  @Inject
  private EventRepository eventRepository;

  @Mappings({
        @Mapping(source = "socialNetworkBySocialNetworkId.id", target = "socialNetworkId"),
        @Mapping(source = "socialNetworkBySocialNetworkId.name", target = "socialNetworkName"),
        @Mapping(source = "searchCriteriaBySearchCriteriaId.id", target = "searchCriteriaId"),
        @Mapping(source = "searchCriteriaBySearchCriteriaId.searchCriteria", target = "searchCriteria"),
        @Mapping(source = "eventByEventId.id", target = "eventId"),
        @Mapping(source = "eventByEventId.description", target = "eventName")})
  public abstract SocialNetworkPostDTO socialNetworkPostToSocialNetworkPostDTO(
        SocialNetworkPost post);

  @Mappings({
        @Mapping(source = "socialNetworkId", target = "socialNetworkBySocialNetworkId"),
        @Mapping(source = "searchCriteriaId", target = "searchCriteriaBySearchCriteriaId"),
        @Mapping(source = "eventId", target = "eventByEventId")})
  public abstract SocialNetworkPost socialNetworkPostDTOToSocialNetworkPost(
        SocialNetworkPostDTO postDTO);

  /**
   *
   */
  public SocialNetwork socialNetworkPostFromId(Integer id) {
    if (id == null) {
      return null;
    }
    return socialNetworkRepository.findOne(id);
  }
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
  public SearchCriteria searchCriteriaFromId(Long id) {
    if (id == null) {
      return null;
    }
    return searchCriteriaRepository.findOne(id);
  }
}
