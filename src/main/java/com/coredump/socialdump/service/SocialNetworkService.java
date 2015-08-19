package com.coredump.socialdump.service;

import com.coredump.socialdump.repository.SocialNetworkRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;



/**
 * Service class for managing SocialNetworks.
 */
@Service
@Transactional
public class SocialNetworkService {

  private final Logger log = LoggerFactory.getLogger(SocialNetworkService.class);
  @Inject
  private SocialNetworkRepository socialNetworkRepository;

}

