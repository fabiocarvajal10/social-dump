package com.coredump.socialdump.web.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the EventStatus entity.
 */
public class EventStatusDTO implements Serializable {

  private Long id;

  @NotNull
  private String status;

  @NotNull
  private String description;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
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

    EventStatusDTO eventStatusDTO = (EventStatusDTO) o;

    if (!Objects.equals(id, eventStatusDTO.id)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "EventStatusDTO{" +
      "id=" + id +
      ", status='" + status + "'" +
      ", description='" + description + "'" +
      '}';
  }
}
