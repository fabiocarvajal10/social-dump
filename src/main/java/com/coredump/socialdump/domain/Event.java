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
public class Event implements Serializable {
    private long id;
    private Timestamp startDate;
    private Timestamp endDate;
    private String description;
    private Timestamp activatedAt;
    private int postDelay;
    private Organization organizationByOrganizationId;
    private EventStatus eventStatusByStatusId;
    private EventType eventTypeByEventTypeId;
    private Collection<SearchCriteria> searchCriteriasById;
    private Collection<SocialNetworkPost> socialNetworkPostsById;
    private Collection<SocialNetworkRequest> socialNetworkRequestsById;
    private Collection<TemporalAccess> temporalAccessesById;

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
    @Column(name = "startDate", nullable = false)
    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "endDate", nullable = false)
    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
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

    @Basic
    @Column(name = "activatedAt")
    public Timestamp getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(Timestamp activatedAt) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != event.id) return false;
        if (postDelay != event.postDelay) return false;
        if (startDate != null ? !startDate.equals(event.startDate) : event.startDate != null) return false;
        if (endDate != null ? !endDate.equals(event.endDate) : event.endDate != null) return false;
        if (description != null ? !description.equals(event.description) : event.description != null) return false;
        if (activatedAt != null ? !activatedAt.equals(event.activatedAt) : event.activatedAt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (activatedAt != null ? activatedAt.hashCode() : 0);
        result = 31 * result + postDelay;
        return result;
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
    public Collection<SocialNetworkPost> getSocialNetworkPostsById() {
        return socialNetworkPostsById;
    }

    public void setSocialNetworkPostsById(Collection<SocialNetworkPost> socialNetworkPostsById) {
        this.socialNetworkPostsById = socialNetworkPostsById;
    }

    @OneToMany(mappedBy = "eventByEventId")
    public Collection<SocialNetworkRequest> getSocialNetworkRequestsById() {
        return socialNetworkRequestsById;
    }

    public void setSocialNetworkRequestsById(Collection<SocialNetworkRequest> socialNetworkRequestsById) {
        this.socialNetworkRequestsById = socialNetworkRequestsById;
    }

    @OneToMany(mappedBy = "eventByEventId")
    public Collection<TemporalAccess> getTemporalAccessesById() {
        return temporalAccessesById;
    }

    public void setTemporalAccessesById(Collection<TemporalAccess> temporalAccessesById) {
        this.temporalAccessesById = temporalAccessesById;
    }
}
