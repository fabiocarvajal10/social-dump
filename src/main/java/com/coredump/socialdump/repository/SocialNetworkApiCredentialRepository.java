package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.SocialNetworkApiCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialNetworkApiCredentialRepository
  extends JpaRepository<SocialNetworkApiCredential, Long> {
}

