package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.repository.GenericStatusRepository;
import com.coredump.socialdump.web.rest.dto.GenericStatusDTO;
import com.coredump.socialdump.web.rest.mapper.GenericStatusMapper;
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
import java.util.stream.Collectors;

/**
 * REST controller for managing Generic Status.
 * Controlador REST que expone servicios para la manipulacion de estados
 * genericos.
 *
 * @author Esteban
 */
@RestController
@RequestMapping("/api")
public class GenericStatusResource {

  /**
  * Escritor de bitacoras de la clase.
  */
  private final Logger log = LoggerFactory.getLogger(GenericStatusResource.class);

  /**
  * Repositorio de estados genericos.
  */
  @Inject
  private GenericStatusRepository genericStatusRepository;

  /**
  * Traductor de objetos de dominio a objetos de transmision de datos.
  */
  @Inject
  private GenericStatusMapper genericStatusMapper;

  /**
   * GET  /generic-statuses -> get all generic statuses.
   */
  @RequestMapping(value = "/generic-statuses",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<GenericStatusDTO> getAll() {
    log.debug("REST request to get all Generic Status");
    return genericStatusRepository.findAll()
      .stream()
      .map(genericStatusMapper::genericStatusToGenericStatusDTO)
      .collect(Collectors.toList());
  }

  /**
   * GET  /generic-statuses/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/generic-statuses/{id}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<GenericStatusDTO> get(@PathVariable short id) {
    log.debug("REST request to get Generic Status : {}", id);
    return Optional.ofNullable(genericStatusRepository.findOne(id))
      .map(genericStatusMapper::genericStatusToGenericStatusDTO)
      .map(genericStatusDTO -> new ResponseEntity<>(genericStatusDTO,
        HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}
