package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.SearchCriteria;

/**
 * Created by Francisco Milanés Sánchez on 15/07/2015.
 */
public interface FetchableInterface extends Runnable {

  @Override
  void run();

  void prepareFetch(SearchCriteria searchCriteria, int delay);

  void setDelay(int delay);

  void kill();
}
