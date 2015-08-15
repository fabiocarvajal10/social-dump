package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.GenericStatus;
import com.coredump.socialdump.repository.GenericStatusRepository;
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
 * REST controller for managing Generic Status.
 */
@RestController
@RequestMapping("/api")
public class GenericStatusResource {

    private final Logger log = LoggerFactory.getLogger(GenericStatusResource.class);

    @Inject
    private GenericStatusRepository genericStatusRepository;

    /**
     * GET  /generic-statuses -> get all generic statuses.
     */
    @RequestMapping(value = "/generic-statuses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GenericStatus> getAll() {
        log.debug("REST request to get all Generic Status");
        return genericStatusRepository.findAll();
    }

    /**
     * GET  /generic-statuses/:id -> get the "id" generic status.
     */
    @RequestMapping(value = "/generic-statuses/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GenericStatus> get(@PathVariable short id) {
        log.debug("REST request to get Generic Status : {}", id);
        return Optional.ofNullable(genericStatusRepository.findOne(id))
                .map(genericStatus -> new ResponseEntity<>(genericStatus, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
