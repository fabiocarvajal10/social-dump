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
public class SocialNetworkRequest implements Serializable {
    private long id;
    private String targetUrl;
    private String request;
    private String searchCriteria;
    private Timestamp createdAt;
    private Collection<SocialNetworkPost> socialNetworkPostsById;
    private Event eventByEventId;
    private SearchCriteria searchCriteriaBySearchCriteriaId;
    private SocialNetworkEndpoint socialNetworkEndpointByEndpointId;
    private Collection<SocialNetworkResponse> socialNetworkResponsesById;

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
    @Size(max = 255)
    @Column(name = "targetUrl", length = 255, nullable = false)
    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    @Basic
    @Column(name = "request", columnDefinition = "text")
    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    @Basic
    @Size(max = 255)
    @Column(name = "searchCriteria", length = 255)
    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
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

        SocialNetworkRequest that = (SocialNetworkRequest) o;

        if (id != that.id) return false;
        if (targetUrl != null ? !targetUrl.equals(that.targetUrl) : that.targetUrl != null) return false;
        if (request != null ? !request.equals(that.request) : that.request != null) return false;
        if (searchCriteria != null ? !searchCriteria.equals(that.searchCriteria) : that.searchCriteria != null)
            return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (targetUrl != null ? targetUrl.hashCode() : 0);
        result = 31 * result + (request != null ? request.hashCode() : 0);
        result = 31 * result + (searchCriteria != null ? searchCriteria.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "socialNetworkRequestByRequestId")
    public Collection<SocialNetworkPost> getSocialNetworkPostsById() {
        return socialNetworkPostsById;
    }

    public void setSocialNetworkPostsById(Collection<SocialNetworkPost> socialNetworkPostsById) {
        this.socialNetworkPostsById = socialNetworkPostsById;
    }

    @ManyToOne
    @JoinColumn(name = "eventId", referencedColumnName = "id")
    public Event getEventByEventId() {
        return eventByEventId;
    }

    public void setEventByEventId(Event eventByEventId) {
        this.eventByEventId = eventByEventId;
    }

    @ManyToOne
    @JoinColumn(name = "searchCriteriaId", referencedColumnName = "id")
    public SearchCriteria getSearchCriteriaBySearchCriteriaId() {
        return searchCriteriaBySearchCriteriaId;
    }

    public void setSearchCriteriaBySearchCriteriaId(SearchCriteria searchCriteriaBySearchCriteriaId) {
        this.searchCriteriaBySearchCriteriaId = searchCriteriaBySearchCriteriaId;
    }

    @ManyToOne
    @JoinColumn(name = "endpointId", referencedColumnName = "id")
    public SocialNetworkEndpoint getSocialNetworkEndpointByEndpointId() {
        return socialNetworkEndpointByEndpointId;
    }

    public void setSocialNetworkEndpointByEndpointId(SocialNetworkEndpoint socialNetworkEndpointByEndpointId) {
        this.socialNetworkEndpointByEndpointId = socialNetworkEndpointByEndpointId;
    }

    @OneToMany(mappedBy = "socialNetworkRequestByRequestId")
    public Collection<SocialNetworkResponse> getSocialNetworkResponsesById() {
        return socialNetworkResponsesById;
    }

    public void setSocialNetworkResponsesById(Collection<SocialNetworkResponse> socialNetworkResponsesById) {
        this.socialNetworkResponsesById = socialNetworkResponsesById;
    }
}
