package com.coredump.socialdump.security;

import com.coredump.socialdump.domain.User;
import com.coredump.socialdump.repository.UserRepository;
import com.coredump.socialdump.service.TemporalAccessService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class UserDetailsService
    implements org.springframework.security.core.userdetails.UserDetailsService {

  private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

  @Inject
  private UserRepository userRepository;

  @Inject
  private TemporalAccessService temporalAccessService;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(final String login) {
    log.debug("Authenticating {}", login);
    String lowercaseLogin = login.toLowerCase();
    Optional<User> userFromDatabase =  userRepository.findOneByLogin(lowercaseLogin);

    if (!userFromDatabase.isPresent()) {
      userFromDatabase = temporalAccessService.userForTemporalAccess(lowercaseLogin);
    }

    return userFromDatabase.map(user -> {
        if (!user.getActivated()) {
          throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
            .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(lowercaseLogin,
          user.getPassword(),
          grantedAuthorities);
      }).orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin
                                  + " was not found in the database"));
  }
}
