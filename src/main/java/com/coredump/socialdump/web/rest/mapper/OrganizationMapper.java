package com.coredump.socialdump.web.rest.mapper;

import com.coredump.socialdump.domain.Organization;
import com.coredump.socialdump.domain.User;
import com.coredump.socialdump.repository.OrganizationRepository;
import com.coredump.socialdump.repository.UserRepository;
import com.coredump.socialdump.web.rest.dto.OrganizationDTO;

import org.mapstruct.*;

import javax.inject.Inject;

/**
 * Created by fabio on 11/07/15.
 *  Mapper for the entity Organization and its DTO OrganizationDTO.
 */

@Mapper(componentModel = "spring", uses = {})
public abstract class OrganizationMapper {

  @Inject
  private UserRepository userRepository;

  @Mappings({
          @Mapping(source = "userByOwnerId.id", target = "ownerId"),
          @Mapping(source = "userByOwnerId.login", target = "ownerLogin")})
  public abstract OrganizationDTO organizationToOrganizationDTO(Organization organization);

  @Mapping(source = "ownerId", target = "userByOwnerId")
  public abstract Organization organizationDTOToOrganization(OrganizationDTO organizationDTO);

  /**
   * Obtiene un User por un id.
   * @param id id del user
   * @return User
   */
  public User userFromId(Long id) {
    if (id == null) {
      return null;
    }
    return userRepository.findOne(id);
  }
}
