package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.MonitorContact;
import com.coredump.socialdump.domain.TemporalAccess;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TemporalAccessRepository extends JpaRepository<TemporalAccess, Long> {

  Page<TemporalAccess> findAllByEventByEventId(Pageable page, Event event);

  List<TemporalAccess> findAllByEventByEventId(Event event);

  Optional<TemporalAccess> findOneById(Long id);

  TemporalAccess findOneByIdAndEmail(Long id, String email);

  Long deleteByMonitorContactByMonitorContactId(MonitorContact monitorContact);

  List<TemporalAccess> getAllByEventByEventId(Event event);

  List<TemporalAccess> getAllByEventByEventIdAndStartDateAndEndDate(Event event,
                                                                    DateTime starDate, DateTime endDate);

  TemporalAccess findOneByIdAndEventByEventId(Long id, Event event);
}
