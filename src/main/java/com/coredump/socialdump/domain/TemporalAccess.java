package com.coredump.socialdump.domain;

import com.coredump.socialdump.domain.util.CustomDateTimeDeserializer;
import com.coredump.socialdump.domain.util.CustomDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Created by fabio on 09/07/15.
 */
@Entity
public class TemporalAccess implements Serializable {
  private long id;
  private String email;
  private String password;
  private DateTime createdAt;
  private DateTime startDate;
  private DateTime endDate;
  private Event eventByEventId;
  private MonitorContact monitorContactByMonitorContactId;
  private GenericStatus genericStatusByStatusId;

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
  @Column(name = "email", length = 255, nullable = false)
  @Size(max = 255)
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Basic
  @Column(name = "password", length = 80, nullable = false)
  @Size(max = 80)
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Basic
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  @Column(name = "createdAt", nullable = false)
  @JsonSerialize(using = CustomDateTimeSerializer.class)
  @JsonDeserialize(using = CustomDateTimeDeserializer.class)
  public DateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(DateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Basic
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  @Column(name = "startDate", nullable = false)
  @JsonSerialize(using = CustomDateTimeSerializer.class)
  @JsonDeserialize(using = CustomDateTimeDeserializer.class)
  public DateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(DateTime startDate) {
    this.startDate = startDate;
  }

  @Basic
  @Column(name = "endDate", nullable = false)
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  @JsonSerialize(using = CustomDateTimeSerializer.class)
  @JsonDeserialize(using = CustomDateTimeDeserializer.class)
  public DateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(DateTime endDate) {
    this.endDate = endDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TemporalAccess that = (TemporalAccess) o;

    if (id != that.id) return false;
    if (email != null ? !email.equals(that.email) : that.email != null) return false;
    if (password != null ? !password.equals(that.password) : that.password != null) return false;
    if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
    if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
    if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
    result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
    result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
    return result;
  }

  @ManyToOne
  @JoinColumn(name = "eventId", referencedColumnName = "id")
  public Event getEventByEventId() {
    return eventByEventId;
  }

  public void setEventByEventId(Event eventByEventId) {
    this.eventByEventId = eventByEventId;
  }

  @ManyToOne
  @JoinColumn(name = "monitorContactId", referencedColumnName = "id")
  public MonitorContact getMonitorContactByMonitorContactId() {
    return monitorContactByMonitorContactId;
  }

  public void setMonitorContactByMonitorContactId(MonitorContact monitorContactByMonitorContactId) {
    this.monitorContactByMonitorContactId = monitorContactByMonitorContactId;
  }

  @ManyToOne
  @JoinColumn(name = "statusId", referencedColumnName = "id")
  public GenericStatus getGenericStatusByStatusId() {
    return genericStatusByStatusId;
  }

  public void setGenericStatusByStatusId(GenericStatus genericStatusByStatusId) {
    this.genericStatusByStatusId = genericStatusByStatusId;
  }
}
