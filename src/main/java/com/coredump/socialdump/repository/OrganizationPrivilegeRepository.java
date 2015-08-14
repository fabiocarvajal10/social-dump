package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.OrganizationPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationPrivilegeRepository
  extends JpaRepository<OrganizationPrivilege, Integer> {
}

