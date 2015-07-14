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
 * Created by fabio on 13/07/15.
 */

public class EventDTO {

  private Long id;

  @NotNull
  @JsonSerialize(using = CustomDateTimeSerializer.class)
  @JsonDeserialize(using = CustomDateTimeDeserializer.class)
  private DateTime startDate;

  @NotNull
  @JsonSerialize(using = CustomDateTimeSerializer.class)
  @JsonDeserialize(using = CustomDateTimeDeserializer.class)
  private DateTime endDate;

  @Size(min = 1,max = 255)
  private String description;

  @JsonSerialize(using = CustomDateTimeSerializer.class)
  @JsonDeserialize(using = CustomDateTimeDeserializer.class)
  private DateTime activatedAt;
  private int postDelay;
  private Long organizationId;
  private short statusId;
  private int typeId;


  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public DateTime getActivatedAt() {
    return activatedAt;
  }

  public void setActivatedAt(DateTime activatedAt) {
    this.activatedAt = activatedAt;
  }

  public int getPostDelay() {
    return postDelay;
  }

  public void setPostDelay(int postDelay) {
    this.postDelay = postDelay;
  }

  public short getStatusId() {
    return statusId;
  }

  public void setStatusId(short statusId) {
    this.statusId = statusId;
  }

  public Long getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(Long organizationId) {
    this.organizationId = organizationId;
  }

  public DateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(DateTime endDate) {
    this.endDate = endDate;
  }

  public DateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(DateTime startDate) {
    this.startDate = startDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "EventDTO{"
            + "description='" + description + '\''
            + ", startDate=" + startDate
            + ", endDate=" + endDate
            + ", activatedAt=" + activatedAt
            + ", postDelay=" + postDelay
            + '}';
  }


}
