package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.repository.GenericStatusRepository;
import com.coredump.socialdump.repository.SearchCriteriaRepository;
import com.coredump.socialdump.repository.SocialNetworkRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Service class for managing Events.
 */
@Service
@Transactional
public class EventService {

  private final Logger log = LoggerFactory.getLogger(EventService.class);

  @Inject
  private SearchCriteriaRepository searchCriteriaRepository;

  @Inject
  private SocialNetworkRepository socialNetworkRepository;

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
    insertScTest(event);
    log.info("Preparing fetch of hashtags");
    event.setSearchCriteriasById(searchCriteriaRepository.findAllByEventByEventId(event));
    //event.getSearchCriteriasById();
    fetchExecutorService.scheduleFetch(event);
  }

  //Temporal

  private void insertScTest(Event event) {
    SearchCriteria sc = new SearchCriteria();
    sc.setEventByEventId(event);
    sc.setSearchCriteria(event.getDescription());
    sc.setSocialNetworkBySocialNetworkId(socialNetworkRepository.getOne(1));
    sc.setGenericStatusByStatusId(genericStatusRepository.getOne((short) 1));
    searchCriteriaRepository.save(sc);

    SearchCriteria sc2 = new SearchCriteria();
    sc2.setEventByEventId(event);
    sc2.setSearchCriteria("summer");
    sc2.setSocialNetworkBySocialNetworkId(socialNetworkRepository.getOne(2));
    sc2.setGenericStatusByStatusId(genericStatusRepository.getOne((short) 1));
    searchCriteriaRepository.save(sc2);
  }

}

