package com.coredump.socialdump.service;
import com.coredump.socialdump.domain.MonitorContact;
import com.coredump.socialdump.repository.MonitorContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.inject.Inject;

/**
 * Service class for managing MonitorContacts.
 */
@Service
@Transactional
public class MonitorContactService {

  private final Logger log = LoggerFactory.getLogger(MonitorContactService.class);
  @Inject
  private MonitorContactRepository monitorContactRepository;

}

