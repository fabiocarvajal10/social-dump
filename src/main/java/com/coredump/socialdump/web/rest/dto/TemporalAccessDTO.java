package com.coredump.socialdump.web.rest.dto;

import com.coredump.socialdump.domain.util.CustomDateTimeDeserializer;
import com.coredump.socialdump.domain.util.CustomDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.joda.time.DateTime;

/**
 * Created by Franz on 22/07/2015.
 */
public class TemporalAccessDTO {

  private long id;
  private String email;
  private String password;

  @JsonSerialize(using = CustomDateTimeSerializer.class)
  @JsonDeserialize(using = CustomDateTimeDeserializer.class)
  private DateTime createdAt;

  @JsonSerialize(using = CustomDateTimeSerializer.class)
  @JsonDeserialize(using = CustomDateTimeDeserializer.class)
  private DateTime startDate;

  @JsonSerialize(using = CustomDateTimeSerializer.class)
  @JsonDeserialize(using = CustomDateTimeDeserializer.class)
  private DateTime endDate;
  private Long eventId;
  private Long monitorContactId;
  private short genericStatusByStatusId;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public DateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(DateTime createdAt) {
    this.createdAt = createdAt;
  }

  public DateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(DateTime startDate) {
    this.startDate = startDate;
  }

  public DateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(DateTime endDate) {
    this.endDate = endDate;
  }

  public Long getEventId() {
    return eventId;
  }

  public void setEventId(Long eventId) {
    this.eventId = eventId;
  }

  public Long getMonitorContactId() {
    return monitorContactId;
  }

  public void setMonitorContactId(Long monitorContactId) {
    this.monitorContactId = monitorContactId;
  }

  public short getGenericStatusByStatusId() {
    return genericStatusByStatusId;
  }

  public void setGenericStatusByStatusId(short genericStatusByStatusId) {
    this.genericStatusByStatusId = genericStatusByStatusId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TemporalAccessDTO that = (TemporalAccessDTO) o;

    if (getId() != that.getId()) return false;
    if (getGenericStatusByStatusId() != that.getGenericStatusByStatusId()) return false;
    if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null)
      return false;
    if (getPassword() != null ? !getPassword().equals(that.getPassword()) : that.getPassword() != null)
      return false;
    if (getCreatedAt() != null ? !getCreatedAt().equals(that.getCreatedAt()) : that.getCreatedAt() != null)
      return false;
    if (getStartDate() != null ? !getStartDate().equals(that.getStartDate()) : that.getStartDate() != null)
      return false;
    if (getEndDate() != null ? !getEndDate().equals(that.getEndDate()) : that.getEndDate() != null)
      return false;
    if (getEventId() != null ? !getEventId().equals(that.getEventId()) : that.getEventId() != null)
      return false;
    return !(getMonitorContactId() != null ? !getMonitorContactId().equals(that.getMonitorContactId()) : that.getMonitorContactId() != null);

  }

  @Override
  public int hashCode() {
    int result = (int) (getId() ^ (getId() >>> 32));
    result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
    result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
    result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
    result = 31 * result + (getStartDate() != null ? getStartDate().hashCode() : 0);
    result = 31 * result + (getEndDate() != null ? getEndDate().hashCode() : 0);
    result = 31 * result + (getEventId() != null ? getEventId().hashCode() : 0);
    result = 31 * result + (getMonitorContactId() != null ? getMonitorContactId().hashCode() : 0);
    result = 31 * result + (int) getGenericStatusByStatusId();
    return result;
  }

  @Override
  public String toString() {
    return "TemporalAccessDTO{" +
      "id=" + id +
      ", email='" + email + '\'' +
      ", password='" + password + '\'' +
      ", createdAt=" + createdAt +
      ", startDate=" + startDate +
      ", endDate=" + endDate +
      ", eventId=" + eventId +
      ", monitorContactId=" + monitorContactId +
      ", genericStatusByStatusId=" + genericStatusByStatusId +
      '}';
  }
}
