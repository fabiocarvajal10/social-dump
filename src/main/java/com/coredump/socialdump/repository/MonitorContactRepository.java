package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.MonitorContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitorContactRepository
  extends JpaRepository<MonitorContact, Long> {
}

