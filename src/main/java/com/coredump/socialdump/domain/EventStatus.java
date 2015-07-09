package com.coredump.socialdump.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by fabio on 09/07/15.
 */
@Entity
public class EventStatus implements Serializable {
    private short id;
    private String status;
    private String description;
    private Collection<Event> eventsById;

    @Id
    @Column(name = "id", columnDefinition = "smallint(3) unsigned", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    @Basic
    @Size(min = 1, max = 30)
    @Column(name = "status", length = 30, unique = true, nullable = false)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Size(min = 1, max = 80)
    @Column(name = "description", length = 80, nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventStatus that = (EventStatus) o;

        if (id != that.id) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) id;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "eventStatusByStatusId")
    public Collection<Event> getEventsById() {
        return eventsById;
    }

    public void setEventsById(Collection<Event> eventsById) {
        this.eventsById = eventsById;
    }
}
