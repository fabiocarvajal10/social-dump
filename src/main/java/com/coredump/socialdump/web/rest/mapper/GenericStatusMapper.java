package com.coredump.socialdump.web.rest.mapper;

import com.coredump.socialdump.domain.*;
import com.coredump.socialdump.web.rest.dto.GenericStatusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GenericStatus and its DTO GenericStatusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GenericStatusMapper {

    GenericStatusDTO genericStatusToGenericStatusDTO(GenericStatus genericStatus);

    GenericStatus genericStatusDTOToGenericStatus(GenericStatusDTO genericStatusDTO);
}
