package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.MonitorContact;
import com.coredump.socialdump.domain.TemporalAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemporalAccessRepository
  extends JpaRepository<TemporalAccess, Long> {

  Long deleteByMonitorContactByMonitorContactId(MonitorContact monitorContact);

}

