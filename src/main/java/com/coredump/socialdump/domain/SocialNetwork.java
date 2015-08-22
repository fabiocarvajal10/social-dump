package com.coredump.socialdump.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by fabio on 09/07/15.
 */
@Entity
public class SocialNetwork implements Serializable {
  private int id;
  private String name;
  private String url;
  private Collection<SearchCriteria> searchCriteriasById;
  private Collection<SocialNetworkApiCredential> socialNetworkApiCredentialsById;
  private Collection<SocialNetworkPost> socialNetworkPostsById;
  private GenericStatus status;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", columnDefinition = "int(4) unsigned", nullable = false)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Size(min = 1, max = 80)
  @Column(name = "name", length = 80, nullable = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Size(min = 1, max = 250)
  @Column(name = "url", length = 250, nullable = false)
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SocialNetwork that = (SocialNetwork) o;

    if (id != that.id) return false;
    if (name != null ? !name.equals(that.name) : that.name != null)
      return false;
    if (url != null ? !url.equals(that.url) : that.url != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (url != null ? url.hashCode() : 0);
    return result;
  }

  @OneToMany(mappedBy = "socialNetworkBySocialNetworkId")
  public Collection<SearchCriteria> getSearchCriteriasById() {
    return searchCriteriasById;
  }

  public void setSearchCriteriasById(Collection<SearchCriteria> searchCriteriasById) {
    this.searchCriteriasById = searchCriteriasById;
  }

  @OneToMany(mappedBy = "socialNetworkBySocialNetworkId")
  public Collection<SocialNetworkApiCredential> getSocialNetworkApiCredentialsById() {
    return socialNetworkApiCredentialsById;
  }

  public void setSocialNetworkApiCredentialsById(Collection<SocialNetworkApiCredential> socialNetworkApiCredentialsById) {
    this.socialNetworkApiCredentialsById = socialNetworkApiCredentialsById;
  }

  @OneToMany(mappedBy = "socialNetworkBySocialNetworkId")
  public Collection<SocialNetworkPost> getSocialNetworkPostsById() {
    return socialNetworkPostsById;
  }

  public void setSocialNetworkPostsById(Collection<SocialNetworkPost> socialNetworkPostsById) {
    this.socialNetworkPostsById = socialNetworkPostsById;
  }

  /**
   * Devuelve el estado
   *
   * @return estado
   */
  @ManyToOne
  @JoinColumn(name = "statusId", referencedColumnName = "id", nullable = false)
  public GenericStatus getGenericStatusByStatusId() {
    return status;
  }

  /**
   * Asigna el estado
   *
   * @param status estado
   */
  public void setGenericStatusByStatusId(GenericStatus status) {
    this.status = status;
  }
}
