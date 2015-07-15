package com.coredump.socialdump.service;
import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.inject.Inject;

/**
 * Service class for managing Events.
 */
@Service
@Transactional
public class EventService {

  private final Logger log = LoggerFactory.getLogger(EventService.class);
  @Inject
  private EventRepository EventRepository;

}

