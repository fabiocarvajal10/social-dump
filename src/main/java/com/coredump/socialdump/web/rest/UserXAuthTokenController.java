package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.TemporalAccess;
import com.coredump.socialdump.domain.User;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.repository.TemporalAccessRepository;
import com.coredump.socialdump.repository.UserRepository;
import com.coredump.socialdump.security.xauth.Token;
import com.coredump.socialdump.security.xauth.TokenProvider;
import com.coredump.socialdump.service.TemporalAccessService;

import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

import javax.inject.Inject;


@RestController
@RequestMapping("/api")
public class UserXAuthTokenController {

  @Inject
  private TokenProvider tokenProvider;

  @Inject
  private AuthenticationManager authenticationManager;

  @Inject
  private UserDetailsService userDetailsService;

  @Inject
  private TemporalAccessRepository temporalAccessRepository;

  @Inject
  private TemporalAccessService temporalAccessService;

  @Inject
  private EventRepository eventRepository;

  @Inject
  private UserRepository userRepository;

  @Inject
  private PasswordEncoder passwordEncoder;

  @RequestMapping(value = "/authenticate",
      method = RequestMethod.POST)
  @Timed
  public Token authorize(@RequestParam String username,
                         @RequestParam String password) {

    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(username, password);
    Authentication authentication =
        this.authenticationManager.authenticate(token);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetails details =
        this.userDetailsService.loadUserByUsername(username);

    return tokenProvider.createToken(details);

  }

  @RequestMapping(value = "/access",
      method = RequestMethod.POST)
  @Timed
  public ResponseEntity<?> authorizeTemporalAccess(@RequestParam Long id,
      @RequestParam Long eventId, @RequestParam String username, @RequestParam String password) {

    DateTime now = new DateTime();
    TemporalAccess temporalAccess = null;

    temporalAccessService.setTemporalAccessId(id);
    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(username, password);
    Authentication authentication =
        this.authenticationManager.authenticate(token);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetails details =
        this.userDetailsService.loadUserByUsername(username);

    Event event = eventRepository.findOne(eventId);
    temporalAccess = temporalAccessRepository.findOneByIdAndEventByEventId(id, event);

    if (temporalAccess == null) {
      return new ResponseEntity<>("Temporal Access not found", HttpStatus.CONFLICT);
    } else if (now.isBefore(temporalAccess.getStartDate())) {
      return new ResponseEntity<>("Monitor cant access before defined time",
        HttpStatus.CONFLICT);
    } else if (now.isAfter(temporalAccess.getEndDate())) {
      return new ResponseEntity<>("Monitor cant access after defined time",
        HttpStatus.CONFLICT);
    }

    Token accessToken = tokenProvider.createToken(details);

    return new ResponseEntity<>(accessToken, HttpStatus.OK);

  }

  @RequestMapping(value = "/delete",
      method = RequestMethod.POST)
  @Timed
  public ResponseEntity<?> deleteAccount(@RequestParam Long id,
      @RequestParam String password) {

    User user = userRepository.findOne(id);

    if (user == null) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    try {
      UsernamePasswordAuthenticationToken token =
          new UsernamePasswordAuthenticationToken(user.getLogin(), password);
      Authentication authentication =
          this.authenticationManager.authenticate(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      UserDetails details =
          this.userDetailsService.loadUserByUsername(user.getLogin());

    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    String login = user.getLogin();
    String email = user.getEmail();
    String createdAt = user.getCreatedDate().toString();
    String info = login + email + createdAt;
    String encode = passwordEncoder.encode(info);
    Random rn = new Random();
    email = encode.replaceAll("[^a-z0-9]",
        Integer.toString(rn.nextInt(9 - 1 + 1) + 1)).substring(0, 20);
    user.setLogin(encode.replaceAll("[^a-z0-9]", Integer.toString(rn.nextInt(9 - 1 + 1) + 1))
        .substring(0, Math.min(encode.length(), 40)));
    user.setPassword(encode);
    user.setEmail(email + "@" + email + ".com");
    user.setActivated(false);
    userRepository.save(user);

    return new ResponseEntity<>(HttpStatus.OK);

  }
}
