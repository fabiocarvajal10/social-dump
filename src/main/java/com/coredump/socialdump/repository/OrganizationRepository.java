package com.coredump.socialdump.repository;


import com.coredump.socialdump.domain.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Created by fabio on 11/07/15.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

  /**
   * Retorna todas las organizaciones del usuario conectado
   *
   * @param pageable objeto paginable que indica la paginación deseada
   * @return las organizaciones del usuario conectado
   */
  @Query(value =
    "SELECT  organization "
    + "FROM  Organization organization "
    + "WHERE organization.userByOwnerId.login = ?#{principal.username}")
  Page<Organization> findAllForCurrentUser(Pageable pageable);

  /**
   * Retorna una organización por id
   *
   * @param idOrg Identificador de la organización que se dea obtener
   * @return un organización
   */
  @Query(value =
    "SELECT  organization "
    + "FROM  Organization organization "
    + "WHERE organization.userByOwnerId.login = ?#{principal.username} "
    + "  AND organization.id = :idOrg")
  Organization findOneForCurrentAndById(@Param("idOrg") Long idOrg);

  /**
   * Todas las organizaciones de un usuario ordenadas de forma descendente
   *
   * @return lista de organizaciones de un usuario ordenadas de forma descendente
   */
  @Query(value =
    "SELECT  organization "
    + "FROM  Organization organization "
    + "WHERE organization.userByOwnerId.login = ?#{principal.username} "
    + "ORDER BY organization.createdAt DESC")
  List<Organization> findAllForCurrentUserOrderByCreatedAtDesc();

  /**
   * Busca una organización por del nombre del usuario conectado
   *
   * @param organizationName nombre de la organizacion
   * @return Organizacion
   */
  @Query(value =
    "SELECT  organization "
    + "FROM  Organization organization "
    + "WHERE organization.userByOwnerId.login = ?#{principal.username} "
    + "  AND organization.name = :orgName")
  Organization findOneForCurrentUserAndName(@Param("orgName") String organizationName);

}
