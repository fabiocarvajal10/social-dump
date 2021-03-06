package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.*;
import com.coredump.socialdump.repository.*;
import com.coredump.socialdump.web.websocket.EventPublicationService;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.util.Date;
import java.util.List;

/**
 * Created by Franz on 13/07/2015.
 */
@Service
public abstract class SocialNetworkFetch implements FetchableInterface {

  @Inject
  EventPublicationService eventPublicationService;

  @Inject
  private SocialNetworkApiCredentialRepository socialNetworkApiCredentialRepository;

  @Inject
  private SocialNetworkPostRepository socialNetworkPostRepository;

  @Inject
  private EventStatusRepository eventStatusRepository;

  @Inject
  private SearchCriteriaRepository searchCriteriaRepository;

  @Inject
  private EventRepository eventRepository;

  private SearchCriteria searchCriteria;
  private SocialNetworkApiCredential socialNetworkApiCredential;
  private boolean alive;
  private int delay;
  private String name;
  private DateTime endDate;

  public SocialNetworkFetch() {

  }

  /**
   * Método que retorna un SearchCriteria.
   * @return SearchCriteria
   */
  public SearchCriteria getSearchCriteria() {
    return searchCriteria;
  }

  /**
   * Método que prepara el fecth de datos.
   * @param searchCriteria criterio de búsqueda
   * @param delay tiempo de retraso para el delay
   */
  public void prepareFetch(SearchCriteria searchCriteria, int delay) {
    this.searchCriteria = searchCriteria;
    this.setSocialNetworkApiCrededential();
    this.alive = true;
    this.delay = (delay * 1000);
    this.setName(searchCriteria);
    this.setEndDate(searchCriteria.getEventByEventId().getEndDate());
  }


  public SocialNetworkApiCredential getSocialNetworkApiCredential() {
    return socialNetworkApiCredential;
  }

  public SocialNetworkPostRepository getSocialNetworkPostRepository() {
    return socialNetworkPostRepository;
  }

  /** Método que se encarga de asignar credenciales.
   */
  public void setSocialNetworkApiCrededential() {
    if (getSearchCriteria() != null) {
      socialNetworkApiCredential =
        socialNetworkApiCredentialRepository
          .findOneBySocialNetworkBySocialNetworkId(getSearchCriteria()
            .getSocialNetworkBySocialNetworkId());
    }
  }

  /**
   * Método que modifical el campo setIsAlive
   * para determinar si se debe detener la extracción de datos.
   */
  public void kill() {
    setIsAlive(false);
  }

  /**
   * Método que evalúa si el servicio de extracción está activo.
   * @return booleano indicando si el servicio está activo
   */
  protected boolean getIsAlive() {
    return this.alive;
  }

  private void setIsAlive(boolean alive) {
    this.alive = alive;
  }

  public int getDelay() {
    return this.delay;
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }

  private DateTime getEndDate() {
    return this.endDate;
  }

  private void setEndDate(DateTime endDate) {
    this.endDate = endDate;
  }

  protected boolean isEventActive() {
    if (!getEndDate().isAfterNow()) {
      this.stopEvent();
    }

    return getEndDate().isAfterNow();
  }

  protected String getName() {
    return this.name;
  }

  private void setName(SearchCriteria searchCriteria) {
    String orgName = searchCriteria.getEventByEventId().getOrganizationByOrganizationId().getName();
    String eventName = searchCriteria.getEventByEventId().getDescription();
    String searchCriteriaName = searchCriteria.getSearchCriteria();

    this.name = orgName + eventName + searchCriteriaName;
  }

  protected void notifyPublications(List<SocialNetworkPost> postList) {
    try {
      eventPublicationService.showPost(postList);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void activateEvent() {
    EventStatus eventStatus =
      eventStatusRepository.findOneByStatus("Activo");
    Event event = searchCriteria.getEventByEventId();
    event.setEventStatusByStatusId(eventStatus);
    eventRepository.saveAndFlush(event);
  }

  protected void stopEvent() {
    EventStatus eventStatus =
      eventStatusRepository.findOneByStatus("Finalizado");
    Event event = searchCriteria.getEventByEventId();
    event.setEventStatusByStatusId(eventStatus);
    eventRepository.saveAndFlush(event);
  }
}
