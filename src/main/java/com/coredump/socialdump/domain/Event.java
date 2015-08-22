package com.coredump.socialdump.domain;

import com.coredump.socialdump.domain.util.CustomDateTimeDeserializer;
import com.coredump.socialdump.domain.util.CustomDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;


/**
 * Created by fabio on 09/07/15.
 */
@Entity
public class Event implements Serializable {
  private Long id;
  private DateTime startDate;
  private DateTime endDate;
  private String description;
  private DateTime activatedAt;
  private int postDelay;
  private Organization organizationByOrganizationId;
  private EventStatus eventStatusByStatusId;
  private EventType eventTypeByEventTypeId;
  private Collection<SearchCriteria> searchCriteriasById;
  private Collection<TemporalAccess> temporalAccessesById;

  @Id
  @Column(name = "id", columnDefinition = "bigint(15) unsigned", nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  @Column(name = "endDate", nullable = false)
  @JsonSerialize(using = CustomDateTimeSerializer.class)
  @JsonDeserialize(using = CustomDateTimeDeserializer.class)
  public DateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(DateTime endDate) {
    this.endDate = endDate;
  }

  @Basic
  @Size(min = 1, max = 255)
  @Column(name = "description", length = 255, nullable = false)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  @Column(name = "activatedAt")
  @JsonSerialize(using = CustomDateTimeSerializer.class)
  @JsonDeserialize(using = CustomDateTimeDeserializer.class)
  public DateTime getActivatedAt() {
    return activatedAt;
  }

  public void setActivatedAt(DateTime activatedAt) {
    this.activatedAt = activatedAt;
  }

  @Basic
  @Column(name = "postDelay", columnDefinition = "int(6) unsigned", nullable = false)
  public int getPostDelay() {
    return postDelay;
  }

  public void setPostDelay(int postDelay) {
    this.postDelay = postDelay;
  }

  @ManyToOne
  @JoinColumn(name = "organizationId", referencedColumnName = "id", nullable = false)
  public Organization getOrganizationByOrganizationId() {
    return organizationByOrganizationId;
  }

  public void setOrganizationByOrganizationId(Organization organizationByOrganizationId) {
    this.organizationByOrganizationId = organizationByOrganizationId;
  }

  @ManyToOne
  @JoinColumn(name = "statusId", referencedColumnName = "id", nullable = false)
  public EventStatus getEventStatusByStatusId() {
    return eventStatusByStatusId;
  }

  public void setEventStatusByStatusId(EventStatus eventStatusByStatusId) {
    this.eventStatusByStatusId = eventStatusByStatusId;
  }

  @ManyToOne
  @JoinColumn(name = "eventTypeId", referencedColumnName = "id", nullable = false)
  public EventType getEventTypeByEventTypeId() {
    return eventTypeByEventTypeId;
  }

  public void setEventTypeByEventTypeId(EventType eventTypeByEventTypeId) {
    this.eventTypeByEventTypeId = eventTypeByEventTypeId;
  }

  @OneToMany(mappedBy = "eventByEventId")
  public Collection<SearchCriteria> getSearchCriteriasById() {
    return searchCriteriasById;
  }

  public void setSearchCriteriasById(Collection<SearchCriteria> searchCriteriasById) {
    this.searchCriteriasById = searchCriteriasById;
  }

  @OneToMany(mappedBy = "eventByEventId")
  public Collection<TemporalAccess> getTemporalAccessesById() {
    return temporalAccessesById;
  }

  public void setTemporalAccessesById(Collection<TemporalAccess> temporalAccessesById) {
    this.temporalAccessesById = temporalAccessesById;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    Event event = (Event) object;

    if (id.equals(event.getId())) {
      return false;
    }
    if (postDelay != event.postDelay) {
      return false;
    }
    if (startDate != null ? !startDate.equals(event.startDate) : event.startDate != null) {
      return false;
    }
    if (endDate != null ? !endDate.equals(event.endDate) : event.endDate != null) {
      return false;
    }
    if (description != null ? !description.equals(event.description) : event.description != null) {
      return false;
    }
    if (activatedAt != null ? !activatedAt.equals(event.activatedAt) : event.activatedAt != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
