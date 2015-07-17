package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.repository.SearchCriteriaRepository;
import com.coredump.socialdump.web.crawler.InstagramFetch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SearchCriteria.
 */
@RestController
@RequestMapping("/api")
public class SearchCriteriaResource {

  private final Logger log =
    LoggerFactory.getLogger(SearchCriteriaResource.class);

  @Inject
  private SearchCriteriaRepository searchCriteriaRepository;

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


  /**
   * GET  /search-criteria/activate/:id
   */
  @RequestMapping(value = "/search-criteria/activate/{id}",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public String fetchData(@PathVariable long id){
    new Thread(new InstagramFetch(
      searchCriteriaRepository.findOne(id))).start();
    return "Obteniendo info";
  }
}

