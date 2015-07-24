package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.MonitorContact;
import com.coredump.socialdump.domain.Organization;
import com.coredump.socialdump.repository.MonitorContactRepository;
import com.coredump.socialdump.repository.OrganizationRepository;
import com.coredump.socialdump.repository.TemporalAccessRepository;
import com.coredump.socialdump.web.rest.dto.MonitorContactDTO;
import com.coredump.socialdump.web.rest.mapper.MonitorContactMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import java.util.List;
import java.util.Optional;

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
    return monitorContactRepository.findOneByEmail(monitorContactDTO.getEmail())
      .map(monitor -> new ResponseEntity<>("e-mail address already in use",
        HttpStatus.BAD_REQUEST))
        .orElseGet(() -> {
            MonitorContact monitorContact =
                monitorContactMapper.monitorContactDTOToMonitorContact(monitorContactDTO);

            monitorContactRepository.save(monitorContact);
            return new ResponseEntity<>(HttpStatus.CREATED);
          });
  }

  /**
   * PUT  /monitor-contacts -> Updates an existing monitor contact.
   */
  @RequestMapping(value = "/monitor-contacts",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Void> update(@RequestBody MonitorContactDTO monitorContactDTO) {

    MonitorContact monitorContact = monitorContactRepository.findOne(monitorContactDTO.getId());

    if (monitorContact == null) {
      return ResponseEntity.notFound().build();
    }

    monitorContact = monitorContactMapper.monitorContactDTOToMonitorContact(monitorContactDTO);
    monitorContactRepository.save(monitorContact);

    return ResponseEntity.ok().build();
  }

  /**
   * GET  /monitor-contacts -> get all the monitors.
   */
  @RequestMapping(value = "/monitor-contacts",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Transactional(readOnly = true)
  public List<MonitorContact> getAll(@RequestParam(value = "organizationId") Long organizationId) {

    Organization organization = organizationRepository.findOneForCurrentAndById(organizationId);

    return monitorContactRepository.findAllByOrganizationByOrganizationId(organization);
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

