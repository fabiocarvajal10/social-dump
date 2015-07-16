package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.SocialNetworkResponse;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Franz on 15/07/2015.
 */
public interface ResponseRepo extends JpaRepository<SocialNetworkResponse, Long> {
}
