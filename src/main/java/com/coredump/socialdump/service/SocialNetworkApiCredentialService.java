package com.coredump.socialdump.service;
import com.coredump.socialdump.domain.SocialNetworkApiCredential;
import com.coredump.socialdump.repository.SocialNetworkApiCredentialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.inject.Inject;

/**
 * Service class for managing SocialNetworkApiCredentials.
 */
@Service
@Transactional
public class SocialNetworkApiCredentialService {

  private final Logger log = LoggerFactory.getLogger(SocialNetworkApiCredentialService.class);
  @Inject
  private SocialNetworkApiCredentialRepository SocialNetworkApiCredentialRepository;

}

