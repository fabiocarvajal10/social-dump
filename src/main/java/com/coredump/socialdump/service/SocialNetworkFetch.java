package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.domain.SocialNetworkApiCredential;
import com.coredump.socialdump.repository.SocialNetworkApiCredentialRepository;
import com.coredump.socialdump.repository.SocialNetworkPostRepository;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

import javax.inject.Inject;

/**
 * Created by Franz on 13/07/2015.
 */
@Service
public abstract class SocialNetworkFetch implements FetchableInterface {

  @Inject
  private SocialNetworkApiCredentialRepository socialNetworkApiCredentialRepository;

  @Inject
  private SocialNetworkPostRepository socialNetworkPostRepository;

  private SearchCriteria searchCriteria;
  private SocialNetworkApiCredential socialNetworkApiCredential;

  public SocialNetworkFetch() {

  }

  public SearchCriteria getSearchCriteria() {
    return searchCriteria;
  }

  public void setSearchCriteria(SearchCriteria searchCriteria) {
    this.searchCriteria = searchCriteria;
    this.setSocialNetworkApiCrededential();
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
}
