package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.repository.SearchCriteriaRepository;
import com.coredump.socialdump.service.GenericStatusService;
import com.coredump.socialdump.web.rest.dto.SearchCriteriaDTO;
import com.coredump.socialdump.web.rest.mapper.SearchCriteriaMapper;
import com.coredump.socialdump.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador REST para manejar criterios de búsqueda.
 *
 * @author Esteban
 */
@RestController
@RequestMapping("/api")
public class SearchCriteriaResource {

  /**
   * Logueador de bitácoras.
   */
  private final Logger log =
    LoggerFactory.getLogger(SearchCriteriaResource.class);

  /**
   * Repositorio de criterios de búsqueda.
   */
  @Inject
  private SearchCriteriaRepository searchCriteriaRepository;

  /**
   * Mapeador de DTOs y criterios de búsqueda.
   */
  @Inject
  private SearchCriteriaMapper searchCriteriaMapper;

  /**
   * Repository de estados.
   */
  @Inject
  private GenericStatusService statusService;

  /**
   * POST  /search-criteria -> Crea un nuevo criterio de búsqueda.
   *
   * @param searchCriteriaDTO encapsulación del criterio de búsqueda
   * @return representación del nuevo criterio de búsqueda
   */
  @RequestMapping(value = "/search-criteria",
    method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<SearchCriteriaDTO> create(
    @Valid @RequestBody SearchCriteriaDTO searchCriteriaDTO)
    throws URISyntaxException {

    log.debug("REST request to save SearchCriteria : {}",
      searchCriteriaDTO.getId());

    if (searchCriteriaDTO.getId() != null) {
      return ResponseEntity.badRequest()
        .header("Failure", "Un nuevo criterio de búsqueda no puede tener un ID")
        .body(null);
    }

    SearchCriteria searchCriteria = searchCriteriaMapper
      .searchCriteriaDTOToSearchCriteria(searchCriteriaDTO);

    if (searchCriteria.getEventByEventId() == null) {
      return ResponseEntity.badRequest()
        .header("Failure", "Event doesn't exist").body(null);
    }
    if (searchCriteria.getSocialNetworkBySocialNetworkId() == null) {
      return ResponseEntity.badRequest()
        .header("Failure", "Social network doesn't exist").body(null);
    }

    searchCriteria.setGenericStatusByStatusId(statusService.getActive());

    searchCriteriaRepository.save(searchCriteria);

    return ResponseEntity.created(new URI("/api/search-criteria/" +
      searchCriteriaDTO.getId()))
      .body(searchCriteriaMapper.searchCriteriaToSearchCriteriaDTO(searchCriteria));
  }

  /**
   * PUT  /searchCriterias -> Actualiza un criterio de búsqueda existente.
   *
   * @param searchCriteriaDTO encapsulación del criterio de búsqueda.
   * @return representación del nuevo criterio de búsqueda
   */
  @RequestMapping(value = "/search-criteria",
    method = RequestMethod.PUT,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<SearchCriteriaDTO> update(
    @Valid @RequestBody SearchCriteriaDTO searchCriteriaDTO)
    throws URISyntaxException {
    log.debug("REST request to update SearchCriteria : {}", searchCriteriaDTO);
    if (searchCriteriaDTO.getId() == null) {
      return create(searchCriteriaDTO);
    }
    SearchCriteria searchCriteria =
      searchCriteriaMapper.searchCriteriaDTOToSearchCriteria(searchCriteriaDTO);
    SearchCriteria result =
      searchCriteriaRepository.save(searchCriteria);
    return ResponseEntity.ok()
      .body(searchCriteriaMapper.searchCriteriaToSearchCriteriaDTO(result));
  }

  /**
   * GET  /search-criteria -> obtiene todos los criterios de búsqueda
   *
   * @param offset cantidad de registros de offset para la consulta
   * @param limit  límite de la consulta
   * @return lista de criterios de búsqueda
   * @throws URISyntaxException si se forma incorrectamente el URI de respuesta
   */
  @RequestMapping(value = "/search-criteria",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Transactional(readOnly = true)
  public ResponseEntity<List<SearchCriteriaDTO>> getAll(
    @RequestParam(value = "page", required = false) Integer offset,
    @RequestParam(value = "per_page", required = false) Integer limit)
    throws URISyntaxException {
    Page<SearchCriteria> page =
      searchCriteriaRepository.findAll(
        PaginationUtil.generatePageRequest(offset, limit));
    HttpHeaders headers =
      PaginationUtil.generatePaginationHttpHeaders(
        page, "/api/search-criteria", offset, limit);
    return new ResponseEntity<>(page.getContent().stream()
      .map(searchCriteriaMapper::searchCriteriaToSearchCriteriaDTO)
      .collect(
        Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
  }

  /**
   * GET  /searchCriterias/:id -> obtiene el criterio de búsqueda del ID dado.
   *
   * @param id Identificador del criterio de búsqueda.
   * @return resultados de la consulta
   */
  @RequestMapping(value = "/search-criteria/{id}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<SearchCriteriaDTO> get(@PathVariable Long id) {
    log.debug("REST request to get SearchCriteria : {}", id);
    return Optional.ofNullable(searchCriteriaRepository.findOne(id))
      .map(searchCriteriaMapper::searchCriteriaToSearchCriteriaDTO)
      .map(searchCriteriaDTO -> new ResponseEntity<>(
        searchCriteriaDTO,
        HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * DELETE  /searchCriterias/:id -> elimina el criterio de búsqueda
   * identificado por el "id".
   *
   * @param id id del criterio de búsqudda
   */
  @RequestMapping(value = "/search-criteria/{id}",
    method = RequestMethod.DELETE,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public void delete(@PathVariable Long id) {
    log.debug("REST request to delete SearchCriteria : {}", id);
    searchCriteriaRepository.delete(id);
  }

}

