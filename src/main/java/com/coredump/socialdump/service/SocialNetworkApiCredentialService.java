package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.SocialNetwork;
import com.coredump.socialdump.domain.SocialNetworkApiCredential;
import com.coredump.socialdump.repository.SocialNetworkApiCredentialRepository;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class for managing SocialNetworkApiCredentials.
 */
@Service
@Transactional
public class SocialNetworkApiCredentialService {

  /**
   * Logger de la clase
   */
  private final Logger log = LoggerFactory.getLogger(
                               SocialNetworkApiCredentialService.class);

  /**
   * Repositorio de credenciales de redes sociales.
   */
  @Inject
  private SocialNetworkApiCredentialRepository
    socialNetworkApiCredentialRepository;

  /**
   * Devuelve un credencial que esté disponible para su uso.
   * @param socialNetwork Red social de la que se quiere el credencial.
   * @return credencial
   */
  public Optional<SocialNetworkApiCredential> getAvailableCredential(
    SocialNetwork socialNetwork) {
    return socialNetworkApiCredentialRepository.
      findOneBySocialNetworkBySocialNetworkIdOrderByLastRequestAsc(
        socialNetwork);
  }
}

