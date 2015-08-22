package com.coredump.socialdump.web.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by fabio on 7/19/15.
 */

public class SearchCriteriaRequestDTO {


  private Long id;

  @NotNull
  @Size(max = 50)
  private String searchCriteria;

  @NotNull
  private Long eventId;

  @NotNull
  private int socialNetworkId;

  @NotNull
  private short genericStatusId;

  public short getGenericStatusId() {
    return genericStatusId;
  }

  public void setGenericStatusId(short genericStatusId) {
    this.genericStatusId = genericStatusId;
  }

  public int getSocialNetworkId() {
    return socialNetworkId;
  }

  public void setSocialNetworkId(int socialNetworkId) {
    this.socialNetworkId = socialNetworkId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSearchCriteria() {
    return searchCriteria;
  }

  public void setSearchCriteria(String searchCriteria) {
    this.searchCriteria = searchCriteria;
  }

  public Long getEventId() {
    return eventId;
  }

  public void setEventId(Long eventId) {
    this.eventId = eventId;
  }
}
