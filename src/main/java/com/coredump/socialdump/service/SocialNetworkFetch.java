package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.SearchCriteria;

import java.util.concurrent.Callable;

/**
 * Created by Franz on 13/07/2015.
 */
public abstract class SocialNetworkFetch implements Runnable {

  private SearchCriteria searchCriteria;

  public SocialNetworkFetch() {

  }

  public SocialNetworkFetch(SearchCriteria searchCriteria) {
    setSearchCriteria(searchCriteria);
  }

  public SearchCriteria getSearchCriteria() {
    return searchCriteria;
  }

  public void setSearchCriteria(SearchCriteria searchCriteria) {
    this.searchCriteria = searchCriteria;
  }
}
