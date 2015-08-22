package com.coredump.socialdump.web.websocket.dto;

import com.coredump.socialdump.domain.util.CustomTimestampDeserializer;
import com.coredump.socialdump.domain.util.CustomTimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by fabio on 7/18/15.
 */
public class SocialNetworkPostSocketDTO implements Serializable {
  private Long id;

  @NotNull
  @JsonSerialize(using = CustomTimestampSerializer.class)
  @JsonDeserialize(using = CustomTimestampDeserializer.class)
  private Timestamp createdAt;

  @NotNull
  private Long snUserId;

  @NotNull
  private String snUserEmail;
  private String mediaUrl;


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

  private String fullName;

  private String profileImage;

  private String profileUrl;

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getProfileImage() {
    return profileImage;
  }

  public void setProfileImage(String profileImage) {
    this.profileImage = profileImage;
  }

  public String getProfileUrl() {
    return profileUrl;
  }

  public void setProfileUrl(String profileUrl) {
    this.profileUrl = profileUrl;
  }

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

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  public Long getSnUserId() {
    return snUserId;
  }

  public void setSnUserId(Long snUserId) {
    this.snUserId = snUserId;
  }

  public String getSnUserEmail() {
    return snUserEmail;
  }

  public void setSnUserEmail(String snUserEmail) {
    this.snUserEmail = snUserEmail;
  }

  public String getMediaUrl() {
    return mediaUrl;
  }

  public void setMediaUrl(String mediaUrl) {
    this.mediaUrl = mediaUrl;
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

    SocialNetworkPostSocketDTO socialNetworkPostDTO = (SocialNetworkPostSocketDTO) object;

    if (!Objects.equals(id, socialNetworkPostDTO.id)) {
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
