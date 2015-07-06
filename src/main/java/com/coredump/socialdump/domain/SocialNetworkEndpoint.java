package com.coredump.socialdump.domain;

import javax.persistence.*;

/**
 * Created by fabio on 05/07/15.
 */
@Entity
public class SocialNetworkEndpoint {
    private int id;
    private String endpoint;
    private String httpMethod;
    private String description;
    private SocialNetwork socialNetworkBySocialNetworkId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "endpoint")
    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Basic
    @Column(name = "httpMethod")
    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    @Basic
    @Column(name = "description")
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

        SocialNetworkEndpoint that = (SocialNetworkEndpoint) o;

        if (id != that.id) return false;
        if (endpoint != null ? !endpoint.equals(that.endpoint) : that.endpoint != null) return false;
        if (httpMethod != null ? !httpMethod.equals(that.httpMethod) : that.httpMethod != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (endpoint != null ? endpoint.hashCode() : 0);
        result = 31 * result + (httpMethod != null ? httpMethod.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "socialNetworkId", referencedColumnName = "id", nullable = false)
    public SocialNetwork getSocialNetworkBySocialNetworkId() {
        return socialNetworkBySocialNetworkId;
    }

    public void setSocialNetworkBySocialNetworkId(SocialNetwork socialNetworkBySocialNetworkId) {
        this.socialNetworkBySocialNetworkId = socialNetworkBySocialNetworkId;
    }
}
