package com.coredump.socialdump.domain;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by fabio on 05/07/15.
 */
@Entity
public class GenericStatus {
    private short id;
    private String status;
    private String description;
    private Collection<OrganizationMember> organizationMembersById;
    private Collection<SearchCriteria> searchCriteriasById;
    private Collection<SocialNetworkPost> socialNetworkPostsById;
    private Collection<TemporalAccess> temporalAccessesById;
    private Collection<UserSD> usersById;

    @Id
    @Column(name = "id")
    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

        GenericStatus that = (GenericStatus) o;

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

    @OneToMany(mappedBy = "genericStatusByStatusId")
    public Collection<OrganizationMember> getOrganizationMembersById() {
        return organizationMembersById;
    }

    public void setOrganizationMembersById(Collection<OrganizationMember> organizationMembersById) {
        this.organizationMembersById = organizationMembersById;
    }

    @OneToMany(mappedBy = "genericStatusByStatusId")
    public Collection<SearchCriteria> getSearchCriteriasById() {
        return searchCriteriasById;
    }

    public void setSearchCriteriasById(Collection<SearchCriteria> searchCriteriasById) {
        this.searchCriteriasById = searchCriteriasById;
    }

    @OneToMany(mappedBy = "genericStatusByStatusId")
    public Collection<SocialNetworkPost> getSocialNetworkPostsById() {
        return socialNetworkPostsById;
    }

    public void setSocialNetworkPostsById(Collection<SocialNetworkPost> socialNetworkPostsById) {
        this.socialNetworkPostsById = socialNetworkPostsById;
    }

    @OneToMany(mappedBy = "genericStatusByStatusId")
    public Collection<TemporalAccess> getTemporalAccessesById() {
        return temporalAccessesById;
    }

    public void setTemporalAccessesById(Collection<TemporalAccess> temporalAccessesById) {
        this.temporalAccessesById = temporalAccessesById;
    }

    @OneToMany(mappedBy = "genericStatusByStatusId")
    public Collection<UserSD> getUsersById() {
        return usersById;
    }

    public void setUsersById(Collection<UserSD> usersById) {
        this.usersById = usersById;
    }
}
