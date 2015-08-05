package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.SocialNetwork;
import com.coredump.socialdump.domain.SocialNetworkPost;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SocialNetworkPostRepository extends
      JpaRepository<SocialNetworkPost, Long> {
  Page<SocialNetworkPost> findAll(Pageable pageable);

  List<SocialNetworkPost> findByeventByEventId(Event event);

  @Query("Select p.socialNetworkBySocialNetworkId From SocialNetworkPost p Where p.eventByEventId.id in (Select e.id From Event e Where e.organizationByOrganizationId.id = :orgId)")
  List<SocialNetwork> findPostsSocialNetworkIdsByOrg(@Param("orgId") Long orgId);
}

