package com.coredump.socialdump.web.websocket.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by fabio on 7/18/15.
 */
public class SocialNetworkPostDTO   implements Serializable {
  private Long id;

  @NotNull
  private String body;

  @NotNull
  private int socialNetworkId;

  @NotNull
  private String socialNetworkName;

  @NotNull
  private Long searchCriteriaId;

  @NotNull
  private String searchCriteria;

  @NotNull
  private Long eventId;

  @NotNull
  private String eventName;

  public String getSocialNetworkName() {
    return socialNetworkName;
  }

  public void setSocialNetworkName(String socialNetworkName) {
    this.socialNetworkName = socialNetworkName;
  }

  public Long getSearchCriteriaId() {
    return searchCriteriaId;
  }

  public void setSearchCriteriaId(Long searchCriteriaId) {
    this.searchCriteriaId = searchCriteriaId;
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public int getSocialNetworkId() {
    return socialNetworkId;
  }

  public void setSocialNetworkId(int socialNetworkId) {
    this.socialNetworkId = socialNetworkId;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }


  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    SocialNetworkPostDTO socialNetworkPostDTODTO = (SocialNetworkPostDTO) object;

    if ( ! Objects.equals(id, socialNetworkPostDTODTO.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "PostDTO{"
          + "body='" + body + '\''
          + ", socialNetworkName=" + socialNetworkName
          + ", searchCriteria=" + searchCriteria
          + ", eventName=" + eventName
          + '}';
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }
}
