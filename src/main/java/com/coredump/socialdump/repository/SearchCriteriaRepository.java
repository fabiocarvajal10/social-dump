package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.SearchCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchCriteriaRepository
  extends JpaRepository<SearchCriteria, Long> {
}

