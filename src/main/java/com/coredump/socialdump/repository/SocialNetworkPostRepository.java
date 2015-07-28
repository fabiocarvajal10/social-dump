package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.SocialNetworkPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SocialNetworkPostRepository extends
      JpaRepository<SocialNetworkPost, Long> {
  Page<SocialNetworkPost> findAll(Pageable pageable);

  List<SocialNetworkPost> findByeventByEventId(Event event);
}

