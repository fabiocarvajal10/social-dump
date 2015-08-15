package com.coredump.socialdump.domain;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.Size;


/**
 * Created by fabio on 09/07/15.
 */
@Entity
public class EventType implements Serializable {
  private Integer id;
  private String name;
  private String description;
  private Collection<Event> eventsById;

  @Id
  @Column(name = "id", columnDefinition = "int(4) unsigned", nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Basic
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, unique = true, nullable = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Size(min = 1, max = 100)
  @Column(name = "description", length = 100, nullable = false)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    EventType eventType = (EventType) object;

    if (id.equals(eventType.getId())) {
      return false;
    }
    if (name != null ? !name.equals(eventType.name) : eventType.name != null) {
      return false;
    }
    if (description != null ? !description.equals(eventType.description) : eventType.description != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    return result;
  }

  @OneToMany(mappedBy = "eventTypeByEventTypeId")
  public Collection<Event> getEventsById() {
    return eventsById;
  }

  public void setEventsById(Collection<Event> eventsById) {
      this.eventsById = eventsById;
  }
}
