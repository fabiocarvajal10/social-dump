package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.repository.SearchCriteriaRepository;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing SearchCriteria.
 */
@Service
@Transactional
public class SearchCriteriaService {

  private final Logger log = LoggerFactory.getLogger(SearchCriteriaService.class);
  @Inject
  private SearchCriteriaRepository searchCriteriaRepository;

}

