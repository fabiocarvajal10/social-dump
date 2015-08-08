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
 * Crado el 13/07/15.
 * @author fabio
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
   * @param organization organización
   * @return objeto página con eventos
   */
  @Query("from Event e where e.organizationByOrganizationId.id = :orgId and e.eventStatusByStatusId.id != (select s.id from EventStatus s where s.status = 'Cancelado') order by e.startDate desc")
  Page<Event> findAllByOrganizationByOrganizationIdOrderByStartDateDescActive(
    Pageable pageable,
    @Param("orgId") Long orgId);

  @Query("from Event e where e.organizationByOrganizationId.id = :orgId and e.startDate >= :date and e.eventStatusByStatusId.id != (select s.id from EventStatus s where s.status = 'Cancelado') order by e.startDate asc")
  Page<Event> findIncomingEvents(Pageable pageable, @Param("orgId") Long orgId,
      @Param("date") DateTime date);

  @Query("from Event e where e.organizationByOrganizationId.id = :orgId and e.endDate <= :date and e.eventStatusByStatusId.id != (select s.id from EventStatus s where s.status = 'Cancelado') order by e.endDate desc")
  Page<Event> findFinalizedEvents(Pageable pageable, @Param("orgId") Long orgId,
      @Param("date") DateTime date);

  @Query("SELECT new map(e, new EventSummaryDTO(e.id,
                                                (SELECT new snind.eventId),
                                                (SELECT COUNT(*)
                                                 FROM SocialNetworkPost s
                                                 WHERE s.eventId = e.id ))
          FROM Event e ")
  HashMap<Event, EventSummaryDTO> findSummariesOf
}
