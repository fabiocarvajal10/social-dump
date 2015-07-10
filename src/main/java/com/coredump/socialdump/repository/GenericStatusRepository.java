package com.coredump.socialdump.repository;


import com.coredump.socialdump.domain.GenericStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by esteban on 7/5/15.
 */
public interface GenericStatusRepository extends JpaRepository<GenericStatus, Short> {

}


