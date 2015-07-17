package com.coredump.socialdump.service;
import com.coredump.socialdump.domain.EventStatus;
import com.coredump.socialdump.repository.EventStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.inject.Inject;

/**
 * Service class for managing EventStatuses.
 */
@Service
@Transactional
public class EventStatusService {

  private final Logger log = LoggerFactory.getLogger(EventStatusService.class);
  @Inject
  private EventStatusRepository eventStatusRepository;

}

