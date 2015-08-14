package com.coredump.socialdump.repository;

import com.coredump.socialdump.domain.SocialNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SocialNetworkRepository
  extends JpaRepository<SocialNetwork, Integer> {

  @Query(value =
    "FROM SocialNetwork " +
    "WHERE genericStatusByStatusId.id = (" +
      "SELECT id FROM GenericStatus WHERE status = 'Activo')")
  List<SocialNetwork> findAllActive();
}

