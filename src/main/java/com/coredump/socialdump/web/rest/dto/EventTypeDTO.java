package com.coredump.socialdump.web.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the EventType entity.
 */
public class EventTypeDTO implements Serializable {

  private Long id;

  @NotNull
  private String name;

  @NotNull
  private String description;

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EventTypeDTO eventTypeDTO = (EventTypeDTO) o;

    if (!Objects.equals(id, eventTypeDTO.id)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "EventTypeDTO{" +
      "id=" + id +
      ", name='" + name + "'" +
      ", description='" + description + "'" +
      '}';
  }
}
