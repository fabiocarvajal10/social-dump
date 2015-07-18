package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by fabio on 13/07/15.
 */
public interface EventRepository extends JpaRepository<Event, Long> {
  Page<Event> findAllByorganizationByOrganizationId(Pageable pageable,
                                                    Organization organization);

  List<Event> findAllByOrganizationByOrganizationIdOrderByActivatedAtDesc
    (Organization organizationByOrganizationId);
}
