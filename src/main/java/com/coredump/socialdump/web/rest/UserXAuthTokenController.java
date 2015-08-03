package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.TemporalAccess;
import com.coredump.socialdump.repository.TemporalAccessRepository;
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
      @RequestParam String username, @RequestParam String password) {

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

    temporalAccess = temporalAccessRepository.findOne(id);

    if (now.isBefore(temporalAccess.getStartDate())) {
      return new ResponseEntity<>("Monitor cant access before defined time",
        HttpStatus.CONFLICT);
    } else if (now.isAfter(temporalAccess.getEndDate())) {
      return new ResponseEntity<>("Monitor cant access after defined time",
        HttpStatus.CONFLICT);
    }

    Token accessToken = tokenProvider.createToken(details);

    return new ResponseEntity<>(accessToken, HttpStatus.OK);

  }
}
