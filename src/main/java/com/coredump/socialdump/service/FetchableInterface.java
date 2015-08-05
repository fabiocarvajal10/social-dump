package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.domain.SocialNetworkPost;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * Created by Franz on 15/07/2015.
 */
public interface FetchableInterface extends Runnable{

  @Override
  void run();

  void prepareFetch(SearchCriteria searchCriteria, int delay);

  void setDelay(int delay);

  void kill();
}
