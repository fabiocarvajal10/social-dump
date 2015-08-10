package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.GenericStatus;
import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.repository.GenericStatusRepository;
import com.coredump.socialdump.repository.SearchCriteriaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Service class for managing Events.
 */
@Service
@Transactional
public class EventService {

  private final Logger log = LoggerFactory.getLogger(EventService.class);

  @Inject
  private EventRepository eventRepository;

  @Inject
  private SearchCriteriaRepository searchCriteriaRepository;

  @Inject
  private GenericStatusRepository genericStatusRepository;

  @Inject
  private FetchExecutorService fetchExecutorService;

  public List<String>  getSearchCriterias(Event event) {
    return  searchCriteriaRepository.findAllByEventByEventId(event)
          .stream()
          .map(SearchCriteria::getSearchCriteria)
          .collect(Collectors.toCollection(ArrayList::new));
  }


  public void scheduleFetch(Event event) {
    log.info("Preparing fetch of hashtags");
    event.setSearchCriteriasById(searchCriteriaRepository.findAllByEventByEventId(event));
    fetchExecutorService.scheduleFetch(event);
  }

  public boolean stopSync(SearchCriteria searchCriteria) {

    if (fetchExecutorService.stopSynchronization(searchCriteria)) {
      GenericStatus genericStatus = genericStatusRepository.findOne((short) 2);
      searchCriteria.setGenericStatusByStatusId(genericStatus);
      searchCriteriaRepository.save(searchCriteria);
      return true;
    } else {
      return false;
    }
  }

  public void stopAllSync(Event event) {
    fetchExecutorService.killAll(event);
  }

  public boolean modifyDelay(SearchCriteria searchCriteria, int delay) {

    if (fetchExecutorService.modifyDelay(searchCriteria, delay)) {
      return true;
    } else {
      return false;
    }
  }

  public void delayAll(Event event, int delay) {
    fetchExecutorService.delayAll(event, delay);
    event.setPostDelay(delay);
    eventRepository.save(event);
  }

}

