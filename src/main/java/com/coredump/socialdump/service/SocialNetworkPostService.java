package com.coredump.socialdump.service;
import com.coredump.socialdump.domain.SocialNetworkPost;
import com.coredump.socialdump.repository.SocialNetworkPostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.inject.Inject;

/**
 * Service class for managing SocialNetworkPosts.
 */
@Service
@Transactional
public class SocialNetworkPostService {

  private final Logger log = LoggerFactory.getLogger(SocialNetworkPostService.class);
  @Inject
  private SocialNetworkPostRepository SocialNetworkPostRepository;

}

