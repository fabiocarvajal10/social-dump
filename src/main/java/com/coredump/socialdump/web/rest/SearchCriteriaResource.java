package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.repository.SearchCriteriaRepository;
import com.coredump.socialdump.web.rest.dto.SearchCriteriaRequestDTO;
import com.coredump.socialdump.web.rest.mapper.SearchCriteriaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SearchCriteria.
 */
@RestController
@RequestMapping("/api")
public class SearchCriteriaResource {

  private final Logger log = LoggerFactory
        .getLogger(SearchCriteriaResource.class);

  @Inject
  private SearchCriteriaRepository searchCriteriaRepository;

  @Inject
  private SearchCriteriaMapper searchCriteriaMapper;

  /**
   * POST /search-criteria -> post create new SearchCriteria
   */
  @RequestMapping(value = "/search-criteria",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Void> create(@Valid @RequestBody SearchCriteriaRequestDTO searchCriteriaDTO)
        throws URISyntaxException {

    log.debug("REST request to save Event: {}", searchCriteriaDTO.getId());

    if (searchCriteriaDTO.getId() != null) {
      return ResponseEntity.badRequest()
            .header("Failure", "A new search criteria cannot already have an ID").build();
    }

    SearchCriteria searchCriteria = searchCriteriaMapper
          .searchCriteriaRequestDTOToSearchCriteria(searchCriteriaDTO);

    if (searchCriteria.getEventByEventId() == null) {
      return ResponseEntity.badRequest()
            .header("Failure", "Event doesn't exist").build();
    }
    if (searchCriteria.getSocialNetworkBySocialNetworkId() == null) {
      return ResponseEntity.badRequest()
            .header("Failure", "Social network doesn't exist").build();
    }

    if (searchCriteria.getGenericStatusByStatusId() == null) {
      return ResponseEntity.badRequest()
            .header("Failure", "Status doesn't exist").build();
    }

    searchCriteriaRepository.save(searchCriteria);

    return ResponseEntity.created(new URI("/api/search-criteria/"
          + searchCriteriaDTO.getId())) .build();
  }

  /**
   * GET  /search-criteria -> get all SearchCriteria.
   */
  @RequestMapping(value = "/search-criteria",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<SearchCriteria> getAll() {
    log.debug("REST request to get all SearchCriteria");
    return searchCriteriaRepository.findAll();
  }

  /**
   * GET  /search-criteria/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/search-criteria/{id}",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<SearchCriteria> get(
        @PathVariable long id) {
    log.debug("REST request to get SearchCriteria : {}", id);
    return Optional.ofNullable(searchCriteriaRepository.findOne(id))
            .map(SearchCriteria ->
                  new ResponseEntity<>(SearchCriteria, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

}

