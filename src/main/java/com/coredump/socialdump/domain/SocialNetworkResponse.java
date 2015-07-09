package com.coredump.socialdump.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by fabio on 09/07/15.
 */
@Entity
public class SocialNetworkResponse implements Serializable {
    private long id;
    private short status;
    private String headers;
    private String body;
    private Collection<SocialNetworkPost> socialNetworkPostsById;
    private SocialNetworkRequest socialNetworkRequestByRequestId;

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
    @Column(name = "status", columnDefinition = "smallint(3) unsigned", nullable = false)
    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    @Basic
    @Column(name = "headers", columnDefinition = "text")
    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    @Basic
    @Column(name = "body", columnDefinition = "text")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SocialNetworkResponse that = (SocialNetworkResponse) o;

        if (id != that.id) return false;
        if (status != that.status) return false;
        if (headers != null ? !headers.equals(that.headers) : that.headers != null) return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) status;
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "socialNetworkResponseByResponseId")
    public Collection<SocialNetworkPost> getSocialNetworkPostsById() {
        return socialNetworkPostsById;
    }

    public void setSocialNetworkPostsById(Collection<SocialNetworkPost> socialNetworkPostsById) {
        this.socialNetworkPostsById = socialNetworkPostsById;
    }

    @ManyToOne
    @JoinColumn(name = "requestId", referencedColumnName = "id")
    public SocialNetworkRequest getSocialNetworkRequestByRequestId() {
        return socialNetworkRequestByRequestId;
    }

    public void setSocialNetworkRequestByRequestId(SocialNetworkRequest socialNetworkRequestByRequestId) {
        this.socialNetworkRequestByRequestId = socialNetworkRequestByRequestId;
    }
}
