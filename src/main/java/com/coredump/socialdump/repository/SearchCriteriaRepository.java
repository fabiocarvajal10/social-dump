package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.SearchCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchCriteriaRepository
  extends JpaRepository<SearchCriteria, Long> {

  List<SearchCriteria> findAllByEventByEventId(Event event);

  SearchCriteria findOneBySearchCriteriaAndEventByEventId(String searchCriteria,
                                                          Event event);

  List<SearchCriteria> findAllBySearchCriteria(String searchCriteria);

}

