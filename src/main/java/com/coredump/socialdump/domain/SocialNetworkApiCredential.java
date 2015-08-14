package com.coredump.socialdump.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Created by fabio on 09/07/15.
 */
@Entity
public class SocialNetworkApiCredential implements Serializable {
  private long id;
  private String appId;
  private String appSecret;
  private Timestamp lastRequest;
  private String accessToken;
  private SocialNetwork socialNetworkBySocialNetworkId;

  @Id
  @Column(name = "id", columnDefinition = "bigint(15) unsigned", nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Basic
  @JsonIgnore
  @Size(min = 1, max = 100)
  @Column(name = "appId", length = 100, nullable = false)
  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  @Basic
  @JsonIgnore
  @Size(min = 1, max = 100)
  @Column(name = "appSecret", length = 100, nullable = false)
  public String getAppSecret() {
    return appSecret;
  }

  public void setAppSecret(String appSecret) {
    this.appSecret = appSecret;
  }

  @Basic
  @Column(name = "lastRequest")
  public Timestamp getLastRequest() {
    return lastRequest;
  }

  public void setLastRequest(Timestamp lastRequest) {
    this.lastRequest = lastRequest;
  }

  @Basic
  @JsonIgnore
  @Size(min = 1, max = 100)
  @Column(name = "accessToken", length = 100)
  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SocialNetworkApiCredential that = (SocialNetworkApiCredential) o;

    if (id != that.id) return false;
    if (appId != null ? !appId.equals(that.appId) : that.appId != null) return false;
    if (appSecret != null ? !appSecret.equals(that.appSecret) : that.appSecret != null) return false;
    if (lastRequest != null ? !lastRequest.equals(that.lastRequest) : that.lastRequest != null) return false;
    if (accessToken != null ? !accessToken.equals(that.accessToken) : that.accessToken != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (appId != null ? appId.hashCode() : 0);
    result = 31 * result + (appSecret != null ? appSecret.hashCode() : 0);
    result = 31 * result + (lastRequest != null ? lastRequest.hashCode() : 0);
    return result;
  }

  @ManyToOne
  @JoinColumn(name = "socialNetworkId", referencedColumnName = "id", nullable = false)
  public SocialNetwork getSocialNetworkBySocialNetworkId() {
    return socialNetworkBySocialNetworkId;
  }

  public void setSocialNetworkBySocialNetworkId(SocialNetwork socialNetworkBySocialNetworkId) {
    this.socialNetworkBySocialNetworkId = socialNetworkBySocialNetworkId;
  }
}
