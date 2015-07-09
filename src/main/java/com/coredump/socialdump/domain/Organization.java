package com.coredump.socialdump.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by fabio on 09/07/15.
 */
@Entity
public class Organization implements Serializable {
    private long id;
    private String name;
    private Timestamp createdAt;
    private Collection<Event> eventsById;
    private Collection<MonitorContact> monitorContactsById;
    private User jhiUserByOwnerId;
    private Collection<OrganizationMember> organizationMembersById;

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
    @Column(name = "name", length = 100, nullable = false, unique = true)
    @Size(max = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "createdAt", nullable = false)
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "organizationByOrganizationId")
    public Collection<Event> getEventsById() {
        return eventsById;
    }

    public void setEventsById(Collection<Event> eventsById) {
        this.eventsById = eventsById;
    }

    @OneToMany(mappedBy = "organizationByOrganizationId")
    public Collection<MonitorContact> getMonitorContactsById() {
        return monitorContactsById;
    }

    public void setMonitorContactsById(Collection<MonitorContact> monitorContactsById) {
        this.monitorContactsById = monitorContactsById;
    }

    @ManyToOne
    @JoinColumn(name = "ownerId", referencedColumnName = "id", nullable = false)
    public User getJhiUserByOwnerId() {
        return jhiUserByOwnerId;
    }

    public void setJhiUserByOwnerId(User jhiUserByOwnerId) {
        this.jhiUserByOwnerId = jhiUserByOwnerId;
    }

    @OneToMany(mappedBy = "organizationByOrganizationId")
    public Collection<OrganizationMember> getOrganizationMembersById() {
        return organizationMembersById;
    }

    public void setOrganizationMembersById(Collection<OrganizationMember> organizationMembersById) {
        this.organizationMembersById = organizationMembersById;
    }
}
