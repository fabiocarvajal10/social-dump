package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.repository.EventTypeRepository;
import com.coredump.socialdump.web.rest.dto.EventTypeDTO;
import com.coredump.socialdump.web.rest.mapper.EventTypeMapper;
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
 * REST controller for managing EventTypes.
 * Expone servicios del API relacionados a los tipos de evento.
 * Estos servicios pueden ser consumidos por JavaScript.
 *
 * @author Esteban
 */
@RestController
@RequestMapping("/api")
public class EventTypeResource {

  /**
   * Escritor de bitacoras de la clase de tipos de evento.
   */
  private final Logger log =
    LoggerFactory.getLogger(EventTypeResource.class);

  /**
   * Repositorio de tipos de evento.
   */
  @Inject
  private EventTypeRepository eventTypeRepository;

  /**
   * Traductor de objetos de dominio (tipos de eventos) a objetos
   * limpios para ser usados por un API REST consumido por JavaScript.
   */
  @Inject
  private EventTypeMapper eventTypeMapper;

  /**
   * GET  /event-types -> get all EventTypes.
   * Obtiene todos los tipos de evento.
   *
   * @return todos los tipos de evento.
   */
  @RequestMapping(value = "/event-types",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<EventTypeDTO> getAll() {
    log.debug("REST request to get all EventTypes");
    return eventTypeRepository.findAll()
      .stream()
      .map(eventTypeMapper::eventTypeToEventTypeDTO)
      .collect(Collectors.toList());
  }

  /**
   * GET  /event-types/:id -> get the "id" generic status.
   * Obtiene el tipo de evento que tenga un identificador dado.
   *
   * @param id identificador del tipo de evento deseado.
   */
  @RequestMapping(value = "/event-types/{id}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<EventTypeDTO> get(
    @PathVariable Integer id) {
    log.debug("REST request to get EventTypes : {}", id);
    return Optional.ofNullable(eventTypeRepository.findOne(id))
      .map(eventTypeMapper::eventTypeToEventTypeDTO)
      .map(EventType ->
        new ResponseEntity<>(EventType, HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}

