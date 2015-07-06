package com.coredump.socialdump.domain;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by fabio on 05/07/15.
 */
@Entity
public class SocialNetworkPost {
    private long id;
    private Timestamp createdAt;
    private Long snUserId;
    private String snUserEmail;
    private String body;
    private String mediaUrl;
    private SocialNetwork socialNetworkBySocialNetworkId;
    private Event eventByEventId;
    private SearchCriteria searchCriteriaBySearchCriteriaId;
    private GenericStatus genericStatusByStatusId;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "createdAt")
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "snUserId")
    public Long getSnUserId() {
        return snUserId;
    }

    public void setSnUserId(Long snUserId) {
        this.snUserId = snUserId;
    }

    @Basic
    @Column(name = "snUserEmail")
    public String getSnUserEmail() {
        return snUserEmail;
    }

    public void setSnUserEmail(String snUserEmail) {
        this.snUserEmail = snUserEmail;
    }

    @Basic
    @Column(name = "body")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Basic
    @Column(name = "mediaUrl")
    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SocialNetworkPost that = (SocialNetworkPost) o;

        if (id != that.id) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (snUserId != null ? !snUserId.equals(that.snUserId) : that.snUserId != null) return false;
        if (snUserEmail != null ? !snUserEmail.equals(that.snUserEmail) : that.snUserEmail != null) return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        if (mediaUrl != null ? !mediaUrl.equals(that.mediaUrl) : that.mediaUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (snUserId != null ? snUserId.hashCode() : 0);
        result = 31 * result + (snUserEmail != null ? snUserEmail.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (mediaUrl != null ? mediaUrl.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "socialNetworkId", referencedColumnName = "id")
    public SocialNetwork getSocialNetworkBySocialNetworkId() {
        return socialNetworkBySocialNetworkId;
    }

    public void setSocialNetworkBySocialNetworkId(SocialNetwork socialNetworkBySocialNetworkId) {
        this.socialNetworkBySocialNetworkId = socialNetworkBySocialNetworkId;
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
    @JoinColumn(name = "statusId", referencedColumnName = "id", nullable = false)
    public GenericStatus getGenericStatusByStatusId() {
        return genericStatusByStatusId;
    }

    public void setGenericStatusByStatusId(GenericStatus genericStatusByStatusId) {
        this.genericStatusByStatusId = genericStatusByStatusId;
    }
}
