package com.coredump.socialdump.service;
import com.coredump.socialdump.domain.OrganizationFuncionality;
import com.coredump.socialdump.repository.OrganizationFuncionalityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.inject.Inject;

/**
 * Service class for managing OrganizationFuncionalities.
 */
@Service
@Transactional
public class OrganizationFuncionalityService {

  private final Logger log = LoggerFactory.getLogger(OrganizationFuncionalityService.class);
  @Inject
  private OrganizationFuncionalityRepository OrganizationFuncionalityRepository;

}

