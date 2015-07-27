package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by fabio on 13/07/15.
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
}
