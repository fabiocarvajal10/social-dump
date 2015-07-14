package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.repository.GenericStatusRepository;
import com.coredump.socialdump.repository.SearchCriteriaRepository;
import com.coredump.socialdump.repository.SocialNetworkPostRepository;
import com.coredump.socialdump.repository.SocialNetworkRepository;

import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Created by Franz on 14/07/2015.
 */
@Service
@Transactional
public class EventService {

  @Inject
  private SearchCriteriaRepository searchCriteriaRepository;

  @Inject
  private SocialNetworkRepository socialNetworkRepository;

  @Inject
  private GenericStatusRepository genericStatusRepository;

  @Inject
  private FetchExecutorService fetchExecutorService;

  public void scheduleFetch(Event event) {
    insertScTest(event);
    event.setSearchCriteriasById(searchCriteriaRepository.findAllByEventByEventId(event));
    fetchExecutorService.scheduleFetch(event);
  }

  private void insertScTest(Event event) {
    SearchCriteria sc = new SearchCriteria();
    sc.setEventByEventId(event);
    sc.setSearchCriteria("HarryBeCareful");
    sc.setSocialNetworkBySocialNetworkId(socialNetworkRepository.getOne(1));
    sc.setGenericStatusByStatusId(genericStatusRepository.getOne((short) 1));
    searchCriteriaRepository.save(sc);
  }
}
