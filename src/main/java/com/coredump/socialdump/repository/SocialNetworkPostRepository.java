package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.SocialNetworkPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialNetworkPostRepository
  extends JpaRepository<SocialNetworkPost, Long> {
}

