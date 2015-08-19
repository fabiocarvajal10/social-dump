package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.Organization;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio de Eventos registrados por usuarios que serán utilizados como
 * base para la obtención de Posts de las redes sociales.
 * Crado el 13/07/15.
 * @author Fabio
 * @author Esteban Trejos
 * @author Francisco
 * @see com.coredump.socialdump.domain.Event Eventos de usuarios
 */
public interface EventRepository extends JpaRepository<Event, Long> {
  Page<Event> findAllByorganizationByOrganizationId(Pageable pageable,
                                                    Organization organization);

  /**
   * Devuelve todas los eventos de una organización, ordenado por fecha de
   * activación de manera descendiente.
   * @param organizationByOrganizationId organización
   * @return lista de eventos
   */
  List<Event> findAllByOrganizationByOrganizationIdOrderByActivatedAtDesc(
    Organization organizationByOrganizationId);

  /**
   * Devuelve todas los eventos de una organización, ordenado por fecha de
   * inicio de manera descendiente.
   * @param pageable objeto paginable que indica la paginación deseada
   * @param organization organización
   * @return objeto página con eventos
   */
  Page<Event> findAllByOrganizationByOrganizationIdOrderByStartDateDesc(
    Pageable pageable, Organization organization);

  /**
   * Devuelve todas los eventos de una organización, ordenado por fecha de
   * inicio de manera descendiente, sin incluir eventos cancelados.
   * @param pageable objeto paginable que indica la paginación deseada
   * @param orgId organización
   * @return objeto página con eventos
   */
  @Query(value =
    "FROM Event e " +
    "WHERE e.organizationByOrganizationId.id = :orgId " +
    "  and e.eventStatusByStatusId.id !=" +
      "(SELECT s.id " +
       "FROM EventStatus s " +
       "WHERE s.status = 'Cancelado')" +
       "ORDER BY e.startDate DESC")
  Page<Event> findAllByOrganizationByOrganizationIdOrderByStartDateDescActive(
    Pageable pageable,
    @Param("orgId") Long orgId);

  /**
   * Devuelve todos los eventos en desarrollo de una organización
   * @param pageable Objeto paginable
   * @param orgId Id de la organización de la cual se quieren buscar los eventos
   * @param date Fecha actual
   * @return Lista con todos los eventos en desarollo
   */
  @Query(
    "FROM Event e "
    + "WHERE e.organizationByOrganizationId.id = :orgId"
    + "  AND e.startDate <= :date AND e.endDate >= :date"
    + "  AND e.eventStatusByStatusId.id != ("
    + "   SELECT s.id"
    + "   FROM EventStatus s"
    + "   WHERE s.status = 'Cancelado')"
    + "   ORDER by e.startDate ASC")
  Page<Event> findIncomingEvents(Pageable pageable, @Param("orgId") Long orgId,
                                 @Param("date") DateTime date);

  /**
   * Devuelve todos los eventos que han finalizado de una organización
   * @param pageable Objeto paginable
   * @param orgId  Id de la organización
   * @param date Fecha actual
   * @return Lista con todos los eventos finalizados
   */
  @Query(value =
    "FROM Event e " +
    "WHERE e.organizationByOrganizationId.id = :orgId " +
    "  AND e.endDate <= :date " +
    "  AND e.eventStatusByStatusId.id !=" +
        "(SELECT s.id " +
         "FROM EventStatus s " +
         "WHERE s.status = 'Cancelado') " +
         "ORDER BY e.endDate DESC")
  Page<Event> findFinalizedEvents(Pageable pageable, @Param("orgId") Long orgId,
                                  @Param("date") DateTime date);

}
