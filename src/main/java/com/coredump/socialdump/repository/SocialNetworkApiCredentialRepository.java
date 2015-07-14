package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.SocialNetworkApiCredential;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Franz on 14/07/2015.
 */
public interface SocialNetworkApiCredentialRepository extends
    JpaRepository<SocialNetworkApiCredential, Long> {


}
