package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.MonitorContact;
import com.coredump.socialdump.repository.MonitorContactRepository;
import com.coredump.socialdump.web.rest.dto.MonitorContactDTO;
import com.coredump.socialdump.web.rest.mapper.EventMapper;
import com.coredump.socialdump.web.rest.mapper.MonitorContactMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

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
  private MonitorContact monitorContactMapper;

  /**
   * POST  /register -> register the monitor contact.
   */
  @RequestMapping(value = "/monitor-contacts",
      method = RequestMethod.POST,
      produces = MediaType.TEXT_PLAIN_VALUE)
  @Timed
  public ResponseEntity<?> registerMonitorContact(@RequestBody MonitorContactDTO
                                             monitorContactDTO, HttpServletRequest request) {
    return monitorContactRepository.findOneByEmail(monitorContactDTO.getEmail())
      .map(monitor -> new ResponseEntity<>("e-mail address already in use",
        HttpStatus.BAD_REQUEST))
        .orElseGet(() -> {
            /*MonitorContact monitorContact =
            monitorContactMapper.monitorContactDTOToMonitorContact(monitorContactDTO);

            monitorContactRepository.save(monitorContact);*/
            //mailService.sendActivationEmail(user, baseUrl);
            return new ResponseEntity<>(HttpStatus.CREATED);
          });
  }

  /**
   * GET  /monitor-contacts -> get all MonitorContacts.
   */
  @RequestMapping(value = "/monitor-contacts",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<MonitorContact> getAll() {
    log.debug("REST request to get all MonitorContacts");
    return monitorContactRepository.findAll();
  }

  /**
   * GET  /monitor-contacts/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/monitor-contacts/{id}",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<MonitorContact> get(
    @PathVariable long id) {
    log.debug("REST request to get MonitorContacts : {}", id);
    return Optional.ofNullable(monitorContactRepository.findOne(id))
            .map(MonitorContact ->
                  new ResponseEntity<>(MonitorContact, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}

