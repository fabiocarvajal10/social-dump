package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.OrganizationRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRoleRepository
  extends JpaRepository<OrganizationRole, Short> {
}

