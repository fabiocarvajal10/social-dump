package com.coredump.socialdump.service;

import com.coredump.socialdump.repository.EventTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service class for managing EventTypes.
 */
@Service
@Transactional
public class EventTypeService {

  private final Logger log = LoggerFactory.getLogger(EventTypeService.class);
  @Inject
  private EventTypeRepository eventTypeRepository;

}

