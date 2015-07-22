package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.EventStatus;
import com.coredump.socialdump.domain.Organization;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.repository.EventStatusRepository;
import com.coredump.socialdump.service.EventService;
import com.coredump.socialdump.service.EventStatusService;
import com.coredump.socialdump.service.OrganizationService;
import com.coredump.socialdump.web.rest.dto.EventDTO;
import com.coredump.socialdump.web.rest.mapper.EventMapper;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.Valid;



/**
 * Created by fabio on 13/07/15.
 */
@RestController
@RequestMapping("/api")
public class EventResource{
  private final Logger log = LoggerFactory.getLogger(EventResource.class);

  @Inject
  private EventRepository eventRepository;

  @Inject
  private EventStatusRepository statusRepository;

  @Inject
  private OrganizationService organizationService;

  @Inject
  private EventMapper eventMapper;

  @Inject
  private EventService eventService;

  private Organization validateOwner(Event event) {
    return organizationService.ownsOrganization(event
          .getOrganizationByOrganizationId()
          .getId());
  }

  private Organization validateOwner(Long id) {
    return organizationService.ownsOrganization(id);
  }

  /**
   * POST  /events/activate/:id -> activate the "id" event.
   */
  @RequestMapping(value = "/events/activate/{id}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Void> activate(@Valid @PathVariable Long id) {
    log.debug("REST request to get Event : {}", id);

    Event event = eventRepository.findOne(id);

    if (event == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    if ( validateOwner(event) == null) {
      return ResponseEntity.status(403).body(null);
    }

    eventService.scheduleFetch(event);

    return ResponseEntity.ok().build();
  }


  /**
   * Repositorio de estados de eventos.
   */
  @Inject
  private EventStatusService eventStatusService;

  /**
   * POST /events -> Create a new event.
   */
  @RequestMapping(value = "/events",
          method = RequestMethod.POST,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Void> create(@Valid @RequestBody EventDTO eventDTO)
          throws URISyntaxException {
    log.debug("REST request to save Event: {}", eventDTO.toString());
    if (eventDTO.getId() != null) {
      return ResponseEntity.badRequest()
              .header("Failure", "A new event cannot already have an ID").build();
    }
    Event event = eventMapper.eventDTOToEvent(eventDTO);
    event.setEventStatusByStatusId(eventStatusService.getActive());
    eventRepository.save(event);
    eventService.scheduleFetch(event);
    return ResponseEntity.created(new URI("/api/events/"
            + eventDTO.getId())) .build();
  }

  /**
   * PUT  /events -> Updates an existing event.
   */
  @RequestMapping(value = "/events",
          method = RequestMethod.PUT,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Void> update(@Valid @RequestBody EventDTO eventDTO)
          throws URISyntaxException {
    log.debug("REST request to update Event {}: ",
            eventDTO.toString());

    Event event;
    event = eventRepository.findOne(eventDTO.getId());

    if (event == null) {
      return ResponseEntity.notFound().build();
    }

    if ( validateOwner(event) == null) {
      return ResponseEntity.status(403).build();
    }

    event = eventMapper.eventDTOToEvent(eventDTO);
    eventRepository.save(event);

    return ResponseEntity.ok().build();
  }

  /**
   * GET  /events -> get all the events.
   */
  @RequestMapping(value = "/events",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Transactional(readOnly = true)
  public ResponseEntity<List<EventDTO>> getAll(
          @RequestParam(value = "page" , required = false) Integer offset,
          @RequestParam(value = "per_page", required = false) Integer limit,
          @Valid @RequestParam(value = "organizationId") Long orgId)
          throws URISyntaxException {

    Organization organization = validateOwner(orgId);
    if ( organization == null) {
      return ResponseEntity.status(403).body(null);
    }

    Page<Event> page = eventRepository
            .findAllByorganizationByOrganizationId(
                    PaginationUtil.generatePageRequest(offset, limit),
                    organization);

    HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(page, "/api/events", offset, limit);

    return new ResponseEntity<>(page.getContent().stream()
            .map(eventMapper::eventToEventDTO)
            .collect(Collectors
                    .toCollection(LinkedList::new)), headers, HttpStatus.OK);
  }

  /**
   * GET  /events/:id -> get the "id" event.
   */
  @RequestMapping(value = "/events/{id}",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<EventDTO> get(@Valid @PathVariable Long id) {
    log.debug("REST request to get Event : {}", id);

    Event event = eventRepository.findOne(id);

    if (event == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    if ( validateOwner(event) == null) {
      return ResponseEntity.status(403).body(null);
    }

    return Optional.ofNullable(event)
            .map(eventMapper::eventToEventDTO)
            .map(EventDTO -> new ResponseEntity<>(
                    EventDTO,
                    HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * DELETE  /events/:id -> delete the "id" event.
   */
  @RequestMapping(value = "/events/{id}",
          method = RequestMethod.DELETE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    log.debug("REST request to delete Event : {}", id);

    Event event = eventRepository.findOne(id);

    if (event == null) {
      return ResponseEntity.notFound().build();
    }

    if ( validateOwner(event) == null) {
      return ResponseEntity.status(403).body(null);
    }

    EventStatus status = statusRepository
            .findOneByStatus("Cancelado");

    event.setEventStatusByStatusId(status);
    eventRepository.save(event);
    return ResponseEntity.ok().build();
  }

}
