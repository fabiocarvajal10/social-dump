package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by fabio on 11/07/15.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
  @Override
  void delete(Organization organization);
}