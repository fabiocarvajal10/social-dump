package com.coredump.socialdump.web.rest.dto;

import com.coredump.socialdump.domain.util.CustomDateTimeDeserializer;
import com.coredump.socialdump.domain.util.CustomDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;


/**
 * Created by fabio on 11/07/15.
 */

public class OrganizationDTO {

  private Long id;

  @NotNull
  @Size(max = 50)
  private String name;

  @JsonSerialize(using = CustomDateTimeSerializer.class)
  @JsonDeserialize(using = CustomDateTimeDeserializer.class)
  private DateTime createdAt;

  private Long ownerId;
  private String ownerLogin;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(DateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getOwnerLogin() {
    return ownerLogin;
  }

  public void setOwnerLogin(String ownerLogin) {
    this.ownerLogin = ownerLogin;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Long userId) {
    this.ownerId = userId;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    OrganizationDTO organizationDTO = (OrganizationDTO) object;

    if (!Objects.equals(id, organizationDTO.id)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "OrganizationDTO{"
      + "name='" + name + '\''
      + ", createdAt=" + createdAt
      + '}';
  }
}
