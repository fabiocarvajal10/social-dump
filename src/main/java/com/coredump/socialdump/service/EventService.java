package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.repository.GenericStatusRepository;
import com.coredump.socialdump.repository.ReqRepo;
import com.coredump.socialdump.repository.ResponseRepo;
import com.coredump.socialdump.repository.SearchCriteriaRepository;
import com.coredump.socialdump.repository.SocialNetworkPostRepository;
import com.coredump.socialdump.repository.SocialNetworkRepository;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Franz on 14/07/2015.
 */
@Service
public class EventService {

  @Inject
  private SearchCriteriaRepository searchCriteriaRepository;

  @Inject
  private SocialNetworkRepository socialNetworkRepository;

  @Inject
  private GenericStatusRepository genericStatusRepository;

  @Inject
  private ResponseRepo responseRepo;

  @Inject
  private ReqRepo reqRepo;

  @Inject
  private FetchExecutorService fetchExecutorService;

  @Inject
  private EventRepository eventRepository;

  public void scheduleFetch(Event event) {
    insertScTest(event);
    event.setSearchCriteriasById(searchCriteriaRepository.findAllByEventByEventId(event));
    fetchExecutorService.scheduleFetch(event);
  }

  //Esta aqui para pruebas, ahorita se va
  private void insertScTest(Event event) {
    SearchCriteria sc = new SearchCriteria();
    sc.setEventByEventId(event);
    sc.setSearchCriteria(event.getDescription());
    sc.setSocialNetworkBySocialNetworkId(socialNetworkRepository.getOne(1));
    sc.setGenericStatusByStatusId(genericStatusRepository.getOne((short) 1));
    searchCriteriaRepository.save(sc);
  }

    public <T> T initializeAndUnproxy(T entity) {
        if (entity == null) {
            throw new
                NullPointerException("Entity passed for initialization is null");
        }

        Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer()
                .getImplementation();
        }
        return entity;
    }
}
