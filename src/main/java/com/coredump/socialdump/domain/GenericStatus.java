package com.coredump.socialdump.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * Estados genéricos para las entidades que lo necesiten.
 * Created by fabio on 09/07/15
 *
 * @autor Fabio
 */
@Entity
public class GenericStatus implements Serializable {
  private short id;
  private String status;
  private String description;
  private Collection<OrganizationMember> organizationMembersById;
  private Collection<SearchCriteria> searchCriteriasById;
  private Collection<SocialNetworkPost> socialNetworkPostsById;
  private Collection<TemporalAccess> temporalAccessesById;

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

    GenericStatus that = (GenericStatus) o;

    if (id != that.id) return false;
    if (status != null ? !status.equals(that.status) : that.status != null)
      return false;
    if (description != null ? !description.equals(that.description) : that.description != null)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
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

  @Override
  public String toString() {
    return "GenericStatus{"
      + "status='" + status + '\''
      + ", description='" + description + '\''
      + '}';
  }
}
