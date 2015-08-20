package com.coredump.socialdump.web.rest.mapper;

import com.coredump.socialdump.domain.MonitorContact;
import com.coredump.socialdump.domain.Organization;
import com.coredump.socialdump.repository.OrganizationRepository;
import com.coredump.socialdump.web.rest.dto.MonitorContactDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.inject.Inject;

/**
 * Created by Franz on 20/07/2015.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class MonitorContactMapper {

  @Inject
  private OrganizationRepository organizationRepository;

  @Mapping(source = "organizationId", target = "organizationByOrganizationId")
  public abstract MonitorContact monitorContactDTOToMonitorContact(MonitorContactDTO
                                                                     monitorContactDTO);

  @Mapping(source = "organizationByOrganizationId.id", target = "organizationId")
  public abstract MonitorContactDTO monitorContactToMonitorContactDTO(MonitorContact
                                                                        monitorContact);

  public Organization organizationFromId(Long id) {
    if (id == null) {
      return null;
    }
    return organizationRepository.findOne(id);
  }
}
