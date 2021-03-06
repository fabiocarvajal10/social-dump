package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.EventStatus;
import com.coredump.socialdump.domain.Organization;
import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.repository.EventStatusRepository;
import com.coredump.socialdump.repository.SearchCriteriaRepository;
import com.coredump.socialdump.repository.SocialNetworkPostRepository;
import com.coredump.socialdump.service.*;
import com.coredump.socialdump.web.rest.dto.EventDTO;
import com.coredump.socialdump.web.rest.dto.EventSocialNetworkSummaryDTO;
import com.coredump.socialdump.web.rest.mapper.EventMapper;
import com.coredump.socialdump.web.rest.util.PaginationUtil;
import com.coredump.socialdump.web.rest.util.ValidatorUtil;
import org.joda.time.DateTime;
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
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Controlador REST encargado de procesar las solicitudes provenientes de
 * los consumidores del API.
 * Created by fabio on 13/07/15.
 *
 * @author Esteban
 * @author Fabio
 * @author Francisco
 */
@RestController
@RequestMapping("/api")
public class EventResource {
  private final Logger log = LoggerFactory.getLogger(EventResource.class);

  @Inject
  private EventRepository eventRepository;

  @Inject
  private EventStatusRepository statusRepository;

  @Inject
  private SearchCriteriaRepository searchCriteriaRepository;

  @Inject
  private OrganizationService organizationService;

  @Inject
  private EventMapper eventMapper;

  @Inject
  private EventService eventService;

  /**
   * Repositorio de posts de redes sociales.
   */
  @Inject
  private SocialNetworkPostRepository socialNetworkPostRepository;

  /**
   * Servicio que maneja lógica de obtención de criterios de búsqueda de la base
   * de datos.
   */
  @Inject
  private SearchCriteriaService searchCriteriaService;

  @Inject
  private TemporalAccessService temporalAccessService;
  /**
   * Repositorio de estados de eventos.
   */
  @Inject
  private EventStatusService eventStatusService;

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

    if (validateOwner(event) == null) {
      return ResponseEntity.status(403).body(null);
    }

    event.setEventStatusByStatusId(eventStatusService.getActive());
    eventRepository.save(event);

    eventService.scheduleFetch(event);

    return ResponseEntity.ok().build();
  }

  /**
   * POST /events -> Create a new event.
   */
  @RequestMapping(value = "/events",
    method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<EventDTO> create(@Valid @RequestBody EventDTO eventDTO)
    throws URISyntaxException {
    log.debug("REST request to save Event: {}", eventDTO.toString());
    if (eventDTO.getId() != null) {
      return ResponseEntity.badRequest()
        .header("Failure", "A new event cannot already have an ID")
        .body(null);
    }

    if (eventDTO.getDescription() == null) {
      return ResponseEntity.badRequest()
        .header("Failure", "The description is required")
        .body(null);
    }

    if (eventDTO.getPostDelay() == null) {
      return ResponseEntity.badRequest()
        .header("Failure", "The post delay is required")
        .body(null);
    }

    if (ValidatorUtil.isDateLower(eventDTO.getEndDate(),
      eventDTO.getStartDate())) {
      return ResponseEntity.badRequest()
        .header("Failure", "End date can't be lower than start date")
        .body(null);
    }

    Event event = eventMapper.eventDTOToEvent(eventDTO);

    if (event.getStartDate().isBeforeNow()) {
      event.setEventStatusByStatusId(eventStatusService.getActive());
    } else {
      event.setEventStatusByStatusId(eventStatusService.getPending());
    }


    eventRepository.save(event);
    event.setSearchCriteriasById(searchCriteriaService
      .getSearchCriteriasFromStringList(event, eventDTO.getSearchCriterias()));

    eventService.scheduleFetch(event);

    return ResponseEntity.created(new URI("/api/events/" + eventDTO.getId()))
      .body(eventMapper.eventToEventDTO(event));
  }

  /**
   * PUT  /events -> Updates an existing event.
   */
  @RequestMapping(value = "/events",
    method = RequestMethod.PUT,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Void> update(@Valid @RequestBody EventDTO eventDTO,
                                     HttpServletRequest request)
    throws URISyntaxException {

    log.debug("REST request to update Event {}: ",
      eventDTO.toString());

    Event event;
    event = eventRepository.findOne(eventDTO.getId());

    if (event == null) {
      return ResponseEntity.notFound().build();
    }

    if (validateOwner(event) == null) {
      return ResponseEntity.status(403).build();
    }


    if (ValidatorUtil.isDateLower(eventDTO.getEndDate(),
      eventDTO.getStartDate())) {
      return ResponseEntity.badRequest()
        .header("Failure", "End date can't be lower than start date")
        .build();
    }

    DateTime oldStartDate = event.getStartDate();
    DateTime oldEndDate = event.getEndDate();

    event = eventMapper.eventDTOToEvent(eventDTO);
    event.setSearchCriteriasById(searchCriteriaRepository
      .findAllByEventByEventId(event));
    eventRepository.save(event);
    temporalAccessService.updateAccessDates(event, oldStartDate, oldEndDate,
        request);
    eventService.modifySchedule(event);

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
    @RequestParam(value = "page", required = false) Integer offset,
    @RequestParam(value = "per_page", required = false) Integer limit,
    @Valid @RequestParam(value = "organizationId") Long orgId)
    throws URISyntaxException {

    log.debug("REST request to get all Organizations");

    Organization organization = validateOwner(orgId);
    if (organization == null) {
      return ResponseEntity.status(403).body(null);
    }

    Page<Event> page = eventRepository
      .findAllByOrganizationByOrganizationIdOrderByStartDateDescActive(
        PaginationUtil.generatePageRequest(offset, limit),
        organization.getId());

    HttpHeaders headers = PaginationUtil
      .generatePaginationHttpHeaders(page, "/api/events",
        offset, limit);

    return new ResponseEntity<>(page
      .getContent()
      .stream()
      .map(eventMapper::eventToEventDTO)
      .collect(Collectors.toCollection(LinkedList::new)),
      headers, HttpStatus.OK);
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

    if (validateOwner(event) == null) {
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

    Organization organization = organizationService
      .ownsOrganization(event
        .getOrganizationByOrganizationId()
        .getId());
    log.debug("Organization null for event with id: {}, organization id: {}",
      event.getId(), event.getOrganizationByOrganizationId().getId());

    if (organization == null) {
      log.debug("Organization not found");
      return ResponseEntity.status(403).build();
    }

    if (validateOwner(event) == null) {
      log.debug("Organization owner mismatch");
      return ResponseEntity.status(403).body(null);
    }

    event.setSearchCriteriasById(searchCriteriaRepository
      .findAllByEventByEventId(event));

    eventService.stopAllSync(event);
    EventStatus status = statusRepository
      .findOneByStatus("Cancelado");
    event.setEndDate(new DateTime());

    event.setEventStatusByStatusId(status);
    eventRepository.save(event);
    return ResponseEntity.ok().build();
  }

  /**
   * GET  /events -> get the next 5 incoming events.
   */
  @RequestMapping(value = "/events/incoming",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Transactional(readOnly = true)
  public ResponseEntity<List<EventDTO>> getIncomingEvents(
    @RequestParam(value = "page", required = false) Integer offset,
    @RequestParam(value = "per_page", required = false) Integer limit,
    @Valid @RequestParam(value = "organizationId") Long orgId)
    throws URISyntaxException {

    Organization organization = validateOwner(orgId);
    if (organization == null) {
      return ResponseEntity.status(403).body(null);
    }

    DateTime now = new DateTime();
    Page<Event> page = eventRepository
      .findIncomingEvents(PaginationUtil.generatePageRequest(offset, limit),
        organization.getId(),
        now);

    HttpHeaders headers = PaginationUtil
      .generatePaginationHttpHeaders(page, "/api/events", offset, limit);

    return new ResponseEntity<>(page
      .getContent()
      .stream()
      .limit(5)
      .map(eventMapper::eventToEventDTO)
      .collect(Collectors.toCollection(LinkedList::new)),
      headers, HttpStatus.OK);
  }

  /**
   * GET  /events -> get the last 5 finalized events.
   */
  @RequestMapping(value = "/events/finalized",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Transactional(readOnly = true)
  public ResponseEntity<List<EventDTO>> getFinalizedEvents(
    @RequestParam(value = "page", required = false) Integer offset,
    @RequestParam(value = "per_page", required = false) Integer limit,
    @Valid @RequestParam(value = "organizationId") Long orgId)
    throws URISyntaxException {

    Organization organization = validateOwner(orgId);
    if (organization == null) {
      return ResponseEntity.status(403).body(null);
    }

    DateTime now = new DateTime();
    Page<Event> page = eventRepository
      .findFinalizedEvents(PaginationUtil.generatePageRequest(offset, limit),
        organization.getId(),
        now);

    HttpHeaders headers = PaginationUtil
      .generatePaginationHttpHeaders(page, "/api/events", offset, limit);

    return new ResponseEntity<>(page
      .getContent()
      .stream()
      .limit(5)
      .map(eventMapper::eventToEventDTO)
      .collect(Collectors.toCollection(LinkedList::new)),
      headers, HttpStatus.OK);
  }

  /**
   * GET  /events-public/:id -> get the "id" event.
   */
  @RequestMapping(value = "/events-public/{id}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<EventDTO> getPublic(@Valid @PathVariable Long id) {
    log.debug("REST request to get Event : {}", id);

    Event event = eventRepository.findOne(id);

    if (event == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    List<String> searchCriteriaList = eventService.getSearchCriterias(event);

    EventDTO eventDTO = eventMapper.eventToEventDTO(event);

    eventDTO.setSearchCriterias(searchCriteriaList);

    return new ResponseEntity<>(eventDTO, HttpStatus.OK);

  }

  /**
   * POST  /events/synchronization/kill
   */
  @RequestMapping(value = "/events/synchronization/kill",
    method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> stopSync(
    @RequestParam(value = "eventId") Long eventId,
    @RequestParam(value = "searchCriteria") String searchCriteria) {

    Event event = eventRepository.findOne(eventId);

    if (event == null) {
      return ResponseEntity.notFound().build();
    }

    Organization organization = organizationService
      .ownsOrganization(event.getOrganizationByOrganizationId().getId());

    if (organization == null) {
      return ResponseEntity.status(403).build();
    }

    if (validateOwner(event) == null) {
      return ResponseEntity.status(403).body(null);
    }

    SearchCriteria sc =
      searchCriteriaRepository.findOneBySearchCriteriaAndEventByEventId(
        searchCriteria, event);

    if (sc != null) {
      boolean killed = eventService.stopSync(sc);

      if (killed) {
        return new ResponseEntity<>(HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
      }
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * POST  /events/synchronization/kill/all
   */
  @RequestMapping(value = "/events/synchronization/kill/all",
    method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> stopAllSync(@RequestParam(value = "eventId")
                                       Long eventId) {

    Event event = eventRepository.findOne(eventId);

    if (event == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    event.setSearchCriteriasById(
      searchCriteriaRepository.findAllByEventByEventId(event));
    eventService.stopAllSync(event);

    return new ResponseEntity<>(HttpStatus.OK);

  }

  /**
   * POST  /events/delay
   */
  @RequestMapping(value = "/events/delay",
    method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> changeDelay(
    @RequestParam(value = "searchCriteriaId")
    Long searchCriteriaId, @RequestParam(value = "delay") int delay) {

    SearchCriteria searchCriteria =
      searchCriteriaRepository.findOne(searchCriteriaId);

    if (searchCriteria.getEventByEventId() == null || searchCriteria == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    boolean delayed = eventService.modifyDelay(searchCriteria, delay);

    if (delayed) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

  }

  /**
   * POST  /events/delay-all
   */
  @RequestMapping(value = "/events/delay-all",
    method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> delayAll(@RequestParam(value = "eventId") Long eventId,
                                    @RequestParam(value = "delay") int delay) {

    Event event = eventRepository.findOne(eventId);
    event.setSearchCriteriasById(searchCriteriaRepository.findAllByEventByEventId(event));

    if (event == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    eventService.delayAll(event, delay);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * GET  /events/{id}/sn-summary -> gets the summary of an event.
   *
   * @param id Id del evento
   * @return summary of an event
   */
  @RequestMapping(value = "/events/{id}/sn-summary",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Transactional(readOnly = true)
  public ResponseEntity<List<EventSocialNetworkSummaryDTO>> getSummaryOfEvent(
    @PathVariable long id) {

    log.debug("REST request to get Summary of Event Organization : {}", id);

    List<EventSocialNetworkSummaryDTO> list =
      socialNetworkPostRepository.getSummariesOfEventGroupBySocialNetwork(id);

    if (list.size() == 0)
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  /**
   * Cancel  /events/cancel -> cancel the event
   */
  @RequestMapping(value = "/events/cancel",
    method = RequestMethod.POST,
    produces = MediaType.TEXT_PLAIN_VALUE)
  @Timed
  public ResponseEntity<?> cancel(@RequestParam(value = "id") Long id) {

    Event event = eventRepository.findOne(id);
    DateTime now = new DateTime();

    if (event == null) {
      return ResponseEntity.notFound().build();
    }

    if (event.getEventStatusByStatusId().getId() == 2) {
      return new ResponseEntity<>("The event is already inactive", HttpStatus.CONFLICT);
    }

    if (ValidatorUtil.isDateLower(event.getEndDate(), now)) {
      return new ResponseEntity<>("The event already ended", HttpStatus.CONFLICT);
    }

    if (!ValidatorUtil.isDateLower(now, event.getStartDate())) {
      return new ResponseEntity<>("Cant cancel a started event", HttpStatus.CONFLICT);
    }

    Organization organization = organizationService
      .ownsOrganization(event.getOrganizationByOrganizationId().getId());

    if (organization == null) {
      return ResponseEntity.status(403).build();
    }

    if (validateOwner(event) == null) {
      return ResponseEntity.status(403).body(null);
    }

    event.setSearchCriteriasById(searchCriteriaRepository.findAllByEventByEventId(event));
    eventService.cancelEvent(event);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Validate Ownership /events/owner/validate -> validates the event owner
   */
  @RequestMapping(value = "/events/owner/validate",
    method = RequestMethod.POST,
    produces = MediaType.TEXT_PLAIN_VALUE)
  @Timed
  public ResponseEntity<?> validateOwnerhsip(@RequestParam(value = "id") Long id) {

    Event event = eventRepository.findOne(id);

    if (event == null) {
      return ResponseEntity.notFound().build();
    }

    if (validateOwner(event) == null) {
      return new ResponseEntity<>("Cant access the event", HttpStatus.FORBIDDEN);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
