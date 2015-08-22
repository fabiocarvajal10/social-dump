package com.coredump.socialdump.domain;

import com.coredump.socialdump.domain.util.CustomDateTimeDeserializer;
import com.coredump.socialdump.domain.util.CustomDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Organization implements Serializable {
  private Long id;
  private String name;
  private DateTime createdAt;
  private Collection<Event> eventsById;
  private Collection<MonitorContact> monitorContactsById;
  private User userByOwnerId;
  private Collection<OrganizationMember> organizationMembersById;

  @Id
  @Column(name = "id", columnDefinition = "bigint(15) unsigned", nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "name", length = 100, nullable = false, unique = true)
  @Size(max = 100)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  @JsonSerialize(using = CustomDateTimeSerializer.class)
  @JsonDeserialize(using = CustomDateTimeDeserializer.class)
  @Column(name = "createdAt", nullable = false)
  public DateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(DateTime createdAt) {
    this.createdAt = createdAt;
  }

  @JsonIgnore
  @OneToMany(mappedBy = "organizationByOrganizationId")
  public Collection<Event> getEventsById() {
    return eventsById;
  }

  public void setEventsById(Collection<Event> eventsById) {
    this.eventsById = eventsById;
  }

  @JsonIgnore
  @OneToMany(mappedBy = "organizationByOrganizationId")
  public Collection<MonitorContact> getMonitorContactsById() {
    return monitorContactsById;
  }

  public void setMonitorContactsById(Collection<MonitorContact> monitorContactsById) {
    this.monitorContactsById = monitorContactsById;
  }

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "ownerId", referencedColumnName = "id", nullable = false)
  public User getUserByOwnerId() {
    return userByOwnerId;
  }

  public void setUserByOwnerId(User userByOwnerId) {
    this.userByOwnerId = userByOwnerId;
  }

  @JsonIgnore
  @OneToMany(mappedBy = "organizationByOrganizationId")
  public Collection<OrganizationMember> getOrganizationMembersById() {
    return organizationMembersById;
  }

  public void setOrganizationMembersById(Collection<OrganizationMember> organizationMembersById) {
    this.organizationMembersById = organizationMembersById;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Organization that = (Organization) object;
    if (id.equals(that.getId())) {
      return false;
    }
    if (name != null ? !name.equals(that.name) : that.name != null) {
      return false;
    }
    if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
