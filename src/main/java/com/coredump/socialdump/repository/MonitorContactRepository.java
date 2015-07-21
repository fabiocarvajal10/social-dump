package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.MonitorContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonitorContactRepository
    extends JpaRepository<MonitorContact, Long> {

  Optional<MonitorContact> findOneByEmail(String email);
}

