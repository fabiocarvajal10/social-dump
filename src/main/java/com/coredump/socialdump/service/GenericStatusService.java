package com.coredump.socialdump.service;
import com.coredump.socialdump.domain.GenericStatus;
import com.coredump.socialdump.repository.GenericStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.inject.Inject;

/**
 * Service class for managing GenericStatuses.
 */
@Service
@Transactional
public class GenericStatusService {

  private final Logger log = LoggerFactory.getLogger(GenericStatusService.class);
  @Inject
  private GenericStatusRepository GenericStatusRepository;

}

