package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.SearchCriteria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Franz on 13/07/2015.
 */
public interface SearchCriteriaRepository extends JpaRepository<SearchCriteria, Long> {

  List<SearchCriteria> findAllByEventByEventId(Event event);
}
