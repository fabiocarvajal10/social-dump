package com.coredump.socialdump.service;
import com.coredump.socialdump.domain.OrganizationMember;
import com.coredump.socialdump.repository.OrganizationMemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.inject.Inject;

/**
 * Service class for managing OrganizationMembers.
 */
@Service
@Transactional
public class OrganizationMemberService {

  private final Logger log = LoggerFactory.getLogger(OrganizationMemberService.class);
  @Inject
  private OrganizationMemberRepository OrganizationMemberRepository;

}

