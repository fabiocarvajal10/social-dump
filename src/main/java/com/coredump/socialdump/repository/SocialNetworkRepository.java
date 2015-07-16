package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.SocialNetwork;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialNetworkRepository
  extends JpaRepository<SocialNetwork, Integer> {
}

