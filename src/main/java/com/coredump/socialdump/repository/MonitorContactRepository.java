package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.MonitorContact;
import com.coredump.socialdump.domain.Organization;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MonitorContactRepository
    extends JpaRepository<MonitorContact, Long> {

  List<MonitorContact> findAllByOrganizationByOrganizationId(Organization organization);

  Optional<MonitorContact> findOneById(Long id);

  Optional<MonitorContact> findOneByEmail(String email);

}

