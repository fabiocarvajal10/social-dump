package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.SocialNetworkPost;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Franz on 14/07/2015.
 */
public interface SocialNetworkPostRepository extends JpaRepository<SocialNetworkPost, Long> {
}
