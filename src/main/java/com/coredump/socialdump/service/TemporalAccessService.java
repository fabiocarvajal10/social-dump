package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.TemporalAccess;
import com.coredump.socialdump.repository.TemporalAccessRepository;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing TemporalAccesses.
 */
@Service
@Transactional
public class TemporalAccessService {

  private final Logger log = LoggerFactory.getLogger(TemporalAccessService.class);
  @Inject
  private TemporalAccessRepository temporalAccessRepository;

}

