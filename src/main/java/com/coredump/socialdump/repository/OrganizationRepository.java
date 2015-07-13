package com.coredump.socialdump.repository;


import com.coredump.socialdump.domain.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Created by fabio on 11/07/15.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
  @Query("select organization from Organization organization where organization.userByOwnerId.login = ?#{principal.username}")
  Page<Organization> findAllForCurrentUser(Pageable pageable);
}