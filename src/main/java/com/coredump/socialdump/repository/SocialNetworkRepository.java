package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.SocialNetwork;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Franz on 14/07/2015.
 */
public interface SocialNetworkRepository extends JpaRepository<SocialNetwork, Integer> {
}
