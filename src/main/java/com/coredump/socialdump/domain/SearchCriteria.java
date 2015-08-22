package com.coredump.socialdump.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by fabio on 09/07/15.
 * Criterio de búsqueda para redes sociales
 */
@Entity
public class SearchCriteria implements Serializable {
  /**
   * Identificador
   */
  private Long id;

  /**
   * Criterio de búsqueda actual
   */
  private String searchCriteria;

  /**
   * Evento en que sucede
   */
  private Event eventByEventId;

  /**
   * Red social destino
   */
  private SocialNetwork socialNetworkBySocialNetworkId;

  /**
   * Estado
   */
  private GenericStatus genericStatusByStatusId;

  /**
   * Posts de redes sociales
   */
  private Collection<SocialNetworkPost> socialNetworkPostsById;

  /**
   * Obtiene el identificador
   *
   * @return identificador
   */
  @Id
  @Column(name = "id", columnDefinition = "bigint(15) unsigned", nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Long getId() {
    return id;
  }

  /**
   * Asigna el identificador
   *
   * @param id identificador
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Obtiene el criterio de búsqueda
   *
   * @return criterio de búsqueda
   */
  @Basic
  @Size(min = 1, max = 255)
  @Column(name = "searchCriteria", length = 255, nullable = false)
  public String getSearchCriteria() {
    return searchCriteria;
  }

  /**
   * Asigna el criterio de búsqueda
   *
   * @param searchCriteria criterio de búsqueda
   */
  public void setSearchCriteria(String searchCriteria) {
    this.searchCriteria = searchCriteria;
  }

  /**
   * Determina si una instancia es equivalente a la misma instancia
   *
   * @param o instancia a comparar
   * @return si es equivalente o no
   */
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

  /**
   * Obtiene el código hash
   *
   * @return
   */
  @Override
  public int hashCode() {
    int tmpId = 0;
    if (id != null) {
      tmpId = id.intValue();
    }
    int result = (tmpId ^ (tmpId >>> 32));
    result = 31 * result + (searchCriteria != null ? searchCriteria.hashCode() : 0);
    return result;
  }

  /**
   * Devuelve el evento
   *
   * @return evento
   */
  @ManyToOne
  @JoinColumn(name = "eventId", referencedColumnName = "id", nullable = false)
  public Event getEventByEventId() {
    return eventByEventId;
  }

  /**
   * Asigna el evento
   *
   * @param eventByEventId evento
   */
  public void setEventByEventId(Event eventByEventId) {
    this.eventByEventId = eventByEventId;
  }

  /**
   * Devuelve la red social
   *
   * @return red social
   */
  @ManyToOne
  @JoinColumn(name = "socialNetworkId", referencedColumnName = "id", nullable = false)
  public SocialNetwork getSocialNetworkBySocialNetworkId() {
    return socialNetworkBySocialNetworkId;
  }

  /**
   * Asigna la red social
   *
   * @param socialNetworkBySocialNetworkId red social
   */
  public void setSocialNetworkBySocialNetworkId(SocialNetwork socialNetworkBySocialNetworkId) {
    this.socialNetworkBySocialNetworkId = socialNetworkBySocialNetworkId;
  }

  /**
   * Devuelve el estado
   *
   * @return estado
   */
  @ManyToOne
  @JoinColumn(name = "statusId", referencedColumnName = "id", nullable = false)
  public GenericStatus getGenericStatusByStatusId() {
    return genericStatusByStatusId;
  }

  /**
   * Asigna el estado
   *
   * @param genericStatusByStatusId estado
   */
  public void setGenericStatusByStatusId(GenericStatus genericStatusByStatusId) {
    this.genericStatusByStatusId = genericStatusByStatusId;
  }

  /**
   * Devuelve una colección de posts de redes sociales
   *
   * @return colección
   */
  @OneToMany(mappedBy = "searchCriteriaBySearchCriteriaId")
  public Collection<SocialNetworkPost> getSocialNetworkPostsById() {
    return socialNetworkPostsById;
  }

  /**
   * Asigna una colección de posts de redes sociales
   *
   * @param socialNetworkPostsById colección de posts
   */
  public void setSocialNetworkPostsById(Collection<SocialNetworkPost> socialNetworkPostsById) {
    this.socialNetworkPostsById = socialNetworkPostsById;
  }

  /**
   * Obtiene información de la instancia
   *
   * @return información
   */
  @Override
  public String toString() {
    return "SearchCriteria{" +
      "id=" + id +
      ", searchCriteria='" + searchCriteria + "'" +
      '}';
  }


}
