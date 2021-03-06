package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.Authority;
import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.TemporalAccess;
import com.coredump.socialdump.domain.User;
import com.coredump.socialdump.repository.TemporalAccessRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing TemporalAccesses.
 */
@Service
@Transactional
public class TemporalAccessService {

  private final Logger log = LoggerFactory.getLogger(TemporalAccessService.class);

  private Long temporalAccessId;

  @Inject
  private TemporalAccessRepository temporalAccessRepository;

  @Inject
  private MailService mailService;

  /**
   * Busca un acceso temporal
   *
   * @param login Correo electrónico del contacto monitor
   * @return Optional de tipo User
   */
  public Optional<User> userForTemporalAccess(String login) {

    TemporalAccess temporalAccess =
      temporalAccessRepository.findOneByIdAndEmail(temporalAccessId, login);

    if (temporalAccess == null) {
      return Optional.empty();
    }

    User tempUser = new User();
    Authority authority = new Authority();
    Set<Authority> authorityList = new HashSet<>();

    authority.setName("ROLE_MONITOR");
    authorityList.add(authority);
    tempUser.setLogin(temporalAccess.getEmail());
    tempUser.setActivated(true);
    tempUser.setPassword(temporalAccess.getPassword());
    tempUser.setAuthorities(authorityList);

    Optional<User> user = Optional.of(tempUser);

    return user;

  }

  /**
   * Asigna el id del acceso temporal
   *
   * @param id Identificador del acceso temporal
   */
  public void setTemporalAccessId(Long id) {
    temporalAccessId = id;
  }

  /**
   * Elimina todos los accesos temporales de un evento
   *
   * @param event Evento del cual se eliminarán los accesos
   */
  public void deleteTemporalAccesses(Event event) {
    temporalAccessRepository.delete(temporalAccessRepository.findAllByEventByEventId(event));
  }

  /**
   * Actualiza la fecha de inicio y fin para todos los accesos temporales de un evento
   *
   * @param event        El evento del cual se buscarán los accesos
   * @param oldStartDate La fecha antigua de inicio
   * @param oldEndDate   La fecha antigua de fin
   * @param request      El request para obtener la base del url
   */
  public void updateAccessDates(Event event, DateTime oldStartDate, DateTime oldEndDate,
                                HttpServletRequest request) {

    List<TemporalAccess> accesses =
      temporalAccessRepository.getAllByEventByEventIdAndStartDateAndEndDate(event, oldStartDate,
        oldEndDate);

    accesses.forEach(ta -> {
      ta.setStartDate(event.getStartDate());
      ta.setEndDate(event.getEndDate());
    });

    temporalAccessRepository.save(accesses);
    sendUpdatedTaEmails(accesses, request);
    //accesses.forEach(ta -> mailService.temporalAccessNewDate(ta, request));
  }

  /**
   * Envía un correo al acceso temporal
   *
   * @param accesses El acceso temporal
   * @param request  El request para obtener la base del url
   */
  private void sendUpdatedTaEmails(List<TemporalAccess> accesses, HttpServletRequest request) {

    accesses.forEach(ta -> {
      String baseUrl =
        request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
          + "/#/access?key=";
      baseUrl += ta.getId() + "&id=" + ta.getEventByEventId().getId();
      mailService.temporalAccessNewDate(ta, baseUrl);
    });
  }
}
