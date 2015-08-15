package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.MonitorContact;
import com.coredump.socialdump.domain.Organization;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonitorContactRepository
    extends JpaRepository<MonitorContact, Long> {

  Page<MonitorContact> findAllByOrganizationByOrganizationId(Pageable pageable,
      Organization organization);

  Optional<MonitorContact> findOneById(Long id);

  Optional<MonitorContact> findOneByEmail(String email);

  Optional<MonitorContact>
      findOneByEmailAndOrganizationByOrganizationId(String email, Organization organization);

}
