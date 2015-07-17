package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Created by fabio on 13/07/15.
 */
public interface EventTypeRepository extends JpaRepository<EventType, Integer> {

}
