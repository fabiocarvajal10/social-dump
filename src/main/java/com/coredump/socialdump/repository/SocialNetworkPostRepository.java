package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.SocialNetwork;
import com.coredump.socialdump.domain.SocialNetworkPost;

import com.coredump.socialdump.web.rest.dto.EventSocialNetworkSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositorio JPA de posts de redes sociales.
 * Especificación para la implementación dinámica generada por Spring.
 * @author Esteban
 * @author Fabio
 */
public interface SocialNetworkPostRepository extends
    JpaRepository<SocialNetworkPost, Long> {

  /**
   * Obtiene todos los posts existentes.
   * @param pageable resultado de la consulta.
   * @return todos los posts existentes.
   */
  Page<SocialNetworkPost> findAll(Pageable pageable);

  /**
   * Obtiene todos los posts de un evento.
   * @param event Evento del que se quieren los posts.
   * @return todos los posts de un evento.
   */
  List<SocialNetworkPost> findByEventByEventId(Event event);

  /**
   * Obtiene los Posts de redes sociales.
   * @param orgId Id de la organización
   * @return todos los Posts
   */
  @Query(value =
    "SELECT p.socialNetworkBySocialNetworkId " +
    "FROM SocialNetworkPost p " +
    "WHERE p.eventByEventId.id IN " +
    "  (SELECT e.id " +
    "   FROM Event e " +
    "   WHERE e.organizationByOrganizationId.id = :orgId)")
  List<SocialNetwork> findPostsSocialNetworkIdsByOrg(@Param("orgId") Long orgId);

  /**
   * Obtiene resúmenes de un evento, agrupados por red social
   * @param eventId Id del evento
   * @return lista de resúmenes de evento por red social
   */
  @Query(value =
    "SELECT "
    + "  new com.coredump.socialdump.web.rest.dto.EventSocialNetworkSummaryDTO("
    + "  s.eventByEventId.id, s.socialNetworkBySocialNetworkId.id,"
    + "  s.socialNetworkBySocialNetworkId.name, COUNT(*)) "
    + "FROM SocialNetworkPost s "
    + "WHERE s.eventByEventId.id = :eventId "
    + "GROUP BY s.socialNetworkBySocialNetworkId.id,"
    + "         s.socialNetworkBySocialNetworkId.name "
    + "ORDER BY s.socialNetworkBySocialNetworkId.id")
  List<EventSocialNetworkSummaryDTO> getSummariesOfEventGroupBySocialNetwork(
    @Param("eventId") long eventId);
}

