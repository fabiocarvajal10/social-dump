package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.OrganizationMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationMemberRepository
  extends JpaRepository<OrganizationMember, Long> {
}

