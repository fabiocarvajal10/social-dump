package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by fabio on 13/07/15.
 */
public interface EventStatusRepository extends JpaRepository<EventStatus, Short> {
  EventStatus findOneByStatus(String status);
}
