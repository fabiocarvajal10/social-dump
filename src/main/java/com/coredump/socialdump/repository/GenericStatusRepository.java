package com.coredump.socialdump.repository;


import com.coredump.socialdump.domain.GenericStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by esteban on 7/5/15.
 */
public interface GenericStatusRepository extends JpaRepository<GenericStatus, Short> {

  Optional<GenericStatus> findOneByStatus(String status);
}


