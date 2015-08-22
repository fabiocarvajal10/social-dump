package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.MonitorContact;
import com.coredump.socialdump.domain.Organization;
import com.coredump.socialdump.repository.MonitorContactRepository;
import com.coredump.socialdump.repository.OrganizationRepository;
import com.coredump.socialdump.repository.TemporalAccessRepository;
import com.coredump.socialdump.web.rest.dto.MonitorContactDTO;
import com.coredump.socialdump.web.rest.mapper.MonitorContactMapper;
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
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing MonitorContacts.
 */
@RestController
@RequestMapping("/api")
public class MonitorContactResource {

  private final Logger log =
    LoggerFactory.getLogger(MonitorContactResource.class);

  @Inject
  private MonitorContactRepository monitorContactRepository;

  @Inject
  private MonitorContactMapper monitorContactMapper;

  @Inject
  private OrganizationRepository organizationRepository;

  @Inject
  private TemporalAccessRepository temporalAccessRepository;

  /**
   * POST  /register -> register the monitor contact.
   */
  @RequestMapping(value = "/monitor-contacts",
    method = RequestMethod.POST,
    produces = MediaType.TEXT_PLAIN_VALUE)
  @Timed
  public ResponseEntity<?> create(@RequestBody MonitorContactDTO monitorContactDTO) {
    MonitorContact monitorContact =
      monitorContactMapper.monitorContactDTOToMonitorContact(monitorContactDTO);

    if (monitorContactDTO.getFirstName() == null ||
        monitorContactDTO.getFirstName().length() == 0) {
      return ResponseEntity.badRequest().build();
    }

    if (monitorContactDTO.getLastName() == null ||
        monitorContactDTO.getLastName().length() == 0) {
      return ResponseEntity.badRequest().build();
    }

    return monitorContactRepository
      .findOneByEmailAndOrganizationByOrganizationId(monitorContact.getEmail(),
        monitorContact.getOrganizationByOrganizationId())
      .map(monitor -> new ResponseEntity<>("e-mail address already in use",
        HttpStatus.CONFLICT))
      .orElseGet(() -> {
        monitorContactRepository.save(monitorContact);
        return new ResponseEntity<>(Long.toString(monitorContact.getId()),
        HttpStatus.CREATED);
      });
  }

  /**
   * PUT  /monitor-contacts -> Updates an existing monitor contact.
   */
  @RequestMapping(value = "/monitor-contacts",
    method = RequestMethod.PUT,
    produces = MediaType.TEXT_PLAIN_VALUE)
  @Timed
  public ResponseEntity<?> update(@RequestBody MonitorContactDTO monitorContactDTO) {

    if (monitorContactRepository.findOne(monitorContactDTO.getId()) == null) {
      return ResponseEntity.notFound().build();
    }

    MonitorContact monitorContact =
      monitorContactMapper.monitorContactDTOToMonitorContact(monitorContactDTO);

    return monitorContactRepository.findOneByEmailAndOrganizationByOrganizationId(
      monitorContact.getEmail(), monitorContact.getOrganizationByOrganizationId())
      .map(monitor -> {
        if (monitor.getId() == monitorContact.getId()) {
          monitorContactRepository.save(monitorContact);
          return new ResponseEntity<>(HttpStatus.OK);
        } else {
          return new ResponseEntity<>("e-mail address already in use",
            HttpStatus.CONFLICT);
        }
      })
      .orElseGet(() -> {
        monitorContactRepository.save(monitorContact);
        return new ResponseEntity<>(HttpStatus.OK);
      });
  }

  /**
   * GET  /monitor-contacts -> get all the monitors.
   */
  @RequestMapping(value = "/monitor-contacts",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Transactional(readOnly = true)
  public ResponseEntity<List<MonitorContactDTO>> getAll(
    @RequestParam(value = "page", required = false) Integer offset,
    @RequestParam(value = "per_page", required = false) Integer limit,
    @RequestParam(value = "organizationId")
    Long organizationId) throws URISyntaxException {

    Organization organization = organizationRepository.findOneForCurrentAndById(organizationId);

    if (organization == null) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    Page<MonitorContact> page = monitorContactRepository
      .findAllByOrganizationByOrganizationId(
        PaginationUtil.generatePageRequest(offset, limit), organization);

    HttpHeaders headers = PaginationUtil
      .generatePaginationHttpHeaders(page, "/api/monitor-contacts", offset, limit);

    return new ResponseEntity<>(page
      .getContent()
      .stream()
      .map(monitorContactMapper::monitorContactToMonitorContactDTO)
      .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
  }

  /**
   * GET  /monitor-contacts/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/monitor-contacts/{id}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<MonitorContact> get(@PathVariable long id) {
    log.debug("REST request to get MonitorContacts : {}", id);
    return Optional.ofNullable(monitorContactRepository.findOne(id))
      .map(MonitorContact ->
        new ResponseEntity<>(MonitorContact, HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * DELETE  /monitor-contacts/:id -> delete the monitor contact.
   */
  @RequestMapping(value = "/monitor-contacts/{id}",
    method = RequestMethod.DELETE,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Transactional
  public ResponseEntity<Void> delete(@PathVariable Long id) {

    MonitorContact monitorContact = monitorContactRepository.findOne(id);

    if (monitorContact == null) {
      return ResponseEntity.notFound().build();
    } else {
      temporalAccessRepository.deleteByMonitorContactByMonitorContactId(monitorContact);
      monitorContactRepository.delete(monitorContact.getId());
      return ResponseEntity.ok().build();
    }

  }

}

