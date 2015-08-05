package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.domain.SocialNetworkApiCredential;
import com.coredump.socialdump.domain.SocialNetworkPost;
import com.coredump.socialdump.repository.SocialNetworkApiCredentialRepository;
import com.coredump.socialdump.repository.SocialNetworkPostRepository;

import com.coredump.socialdump.web.websocket.EventPublicationService;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.inject.Inject;

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

  private SearchCriteria searchCriteria;
  private SocialNetworkApiCredential socialNetworkApiCredential;
  private boolean alive;
  private int delay;
  private String name;

  public SocialNetworkFetch() {

  }

  public SearchCriteria getSearchCriteria() {
    return searchCriteria;
  }

  public void prepareFetch(SearchCriteria searchCriteria, int delay) {
    this.searchCriteria = searchCriteria;
    this.setSocialNetworkApiCrededential();
    this.alive = true;
    this.delay = (delay * 1000);
    this.setName(searchCriteria);
  }

  public SocialNetworkApiCredential getSocialNetworkApiCredential() {
    return socialNetworkApiCredential;
  }

  public SocialNetworkPostRepository getSocialNetworkPostRepository() {
    return socialNetworkPostRepository;
  }

  public void setSocialNetworkApiCrededential() {
    if (getSearchCriteria() != null) {
      socialNetworkApiCredential =
          socialNetworkApiCredentialRepository
              .findOneBySocialNetworkBySocialNetworkId(getSearchCriteria()
                  .getSocialNetworkBySocialNetworkId());
    }
  }

  public void kill() {
    setIsAlive(false);
  }

  protected boolean getIsAlive() {
    return this.alive;
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }

  public int getDelay() {
    return this.delay;
  }

  private void setIsAlive(boolean alive) {
    this.alive = alive;
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
}
