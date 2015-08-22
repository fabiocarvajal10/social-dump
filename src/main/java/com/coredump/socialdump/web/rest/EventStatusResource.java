package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.repository.EventStatusRepository;
import com.coredump.socialdump.web.rest.dto.EventStatusDTO;
import com.coredump.socialdump.web.rest.mapper.EventStatusMapper;
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
 * REST controller for managing EventStatuses.
 * Expone servicios del API relacionados a los estados de eventos.
 * Estos servicios pueden ser consumidos por JavaScript.
 *
 * @author Esteban
 */
@RestController
@RequestMapping("/api")
public class EventStatusResource {

  /**
   * Escritor de bitacoras de la clase de estados de evento.
   */
  private final Logger log =
    LoggerFactory.getLogger(EventStatusResource.class);

  /**
   * Repositorio de estados de evento.
   */
  @Inject
  private EventStatusRepository eventStatusRepository;

  /**
   * Traductor de objetos de dominio (estados de eventos) a objetos
   * limpios para ser usados por un API REST consumido por JavaScript.
   */
  @Inject
  private EventStatusMapper eventStatusMapper;

  /**
   * GET  /event-statuses -> get all EventStatuses.
   * Obtiene todos los estados de evento.
   *
   * @return todos los estados de evento.
   */
  @RequestMapping(value = "/event-statuses",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<EventStatusDTO> getAll() {
    log.debug("REST request to get all EventStatuses");
    return eventStatusRepository.findAll()
      .stream()
      .map(eventStatusMapper::eventStatusToEventStatusDTO)
      .collect(Collectors.toList());
  }

  /**
   * GET  /event-statuses/:id -> get the "id" generic status.
   * Obtiene el estado de evento que tenga un identificador dado.
   *
   * @param id identificador del tipo de evento deseado.
   */
  @RequestMapping(value = "/event-statuses/{id}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<EventStatusDTO> get(
    @PathVariable Short id) {
    log.debug("REST request to get EventStatuses : {}", id);
    return Optional.ofNullable(eventStatusRepository.findOne(id))
      .map(eventStatusMapper::eventStatusToEventStatusDTO)
      .map(eventStatusDTO ->
        new ResponseEntity<>(eventStatusDTO, HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}

