package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.SocialNetwork;
import com.coredump.socialdump.domain.SocialNetworkApiCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialNetworkApiCredentialRepository
  extends JpaRepository<SocialNetworkApiCredential, Long> {

  SocialNetworkApiCredential findOneBySocialNetworkBySocialNetworkId(
    SocialNetwork socialNetwork);

  Optional<SocialNetworkApiCredential>
    findOneBySocialNetworkBySocialNetworkIdOrderByLastRequestAsc(
    SocialNetwork socialNetwork);
}

