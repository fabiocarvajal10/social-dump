package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.Authority;
import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.TemporalAccess;
import com.coredump.socialdump.domain.User;
import com.coredump.socialdump.repository.TemporalAccessRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

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

  public void setTemporalAccessId(Long id) {
    temporalAccessId = id;
  }

  public void deleteTemporalAccesses(Event event) {
    temporalAccessRepository.delete(temporalAccessRepository.findAllByEventByEventId(event));
  }
}

