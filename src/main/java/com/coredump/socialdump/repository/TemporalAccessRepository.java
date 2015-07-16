package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.TemporalAccess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporalAccessRepository
  extends JpaRepository<TemporalAccess, Long> {
}

