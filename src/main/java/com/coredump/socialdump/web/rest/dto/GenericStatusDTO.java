package com.coredump.socialdump.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the GenericStatus entity.
 */
public class GenericStatusDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 30)
    private String status;

    @NotNull
    @Size(min = 1, max = 80)
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

        GenericStatusDTO genericStatusDTO = (GenericStatusDTO) o;

        if ( ! Objects.equals(id, genericStatusDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "GenericStatusDTO{" +
                "id=" + id +
                ", status='" + status + "'" +
                ", description='" + description + "'" +
                '}';
    }
}
