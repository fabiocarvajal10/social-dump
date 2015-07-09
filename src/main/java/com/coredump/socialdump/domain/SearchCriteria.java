package com.coredump.socialdump.domain;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by fabio on 09/07/15.
 */
@Entity
public class SearchCriteria {
    private long id;
    private String searchCriteria;
    private Event eventByEventId;
    private SocialNetwork socialNetworkBySocialNetworkId;
    private GenericStatus genericStatusByStatusId;
    private Collection<SocialNetworkPost> socialNetworkPostsById;
    private Collection<SocialNetworkRequest> socialNetworkRequestsById;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "searchCriteria")
    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchCriteria that = (SearchCriteria) o;

        if (id != that.id) return false;
        if (searchCriteria != null ? !searchCriteria.equals(that.searchCriteria) : that.searchCriteria != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (searchCriteria != null ? searchCriteria.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "eventId", referencedColumnName = "id", nullable = false)
    public Event getEventByEventId() {
        return eventByEventId;
    }

    public void setEventByEventId(Event eventByEventId) {
        this.eventByEventId = eventByEventId;
    }

    @ManyToOne
    @JoinColumn(name = "socialNetworkId", referencedColumnName = "id", nullable = false)
    public SocialNetwork getSocialNetworkBySocialNetworkId() {
        return socialNetworkBySocialNetworkId;
    }

    public void setSocialNetworkBySocialNetworkId(SocialNetwork socialNetworkBySocialNetworkId) {
        this.socialNetworkBySocialNetworkId = socialNetworkBySocialNetworkId;
    }

    @ManyToOne
    @JoinColumn(name = "statusId", referencedColumnName = "id", nullable = false)
    public GenericStatus getGenericStatusByStatusId() {
        return genericStatusByStatusId;
    }

    public void setGenericStatusByStatusId(GenericStatus genericStatusByStatusId) {
        this.genericStatusByStatusId = genericStatusByStatusId;
    }

    @OneToMany(mappedBy = "searchCriteriaBySearchCriteriaId")
    public Collection<SocialNetworkPost> getSocialNetworkPostsById() {
        return socialNetworkPostsById;
    }

    public void setSocialNetworkPostsById(Collection<SocialNetworkPost> socialNetworkPostsById) {
        this.socialNetworkPostsById = socialNetworkPostsById;
    }

    @OneToMany(mappedBy = "searchCriteriaBySearchCriteriaId")
    public Collection<SocialNetworkRequest> getSocialNetworkRequestsById() {
        return socialNetworkRequestsById;
    }

    public void setSocialNetworkRequestsById(Collection<SocialNetworkRequest> socialNetworkRequestsById) {
        this.socialNetworkRequestsById = socialNetworkRequestsById;
    }
}
