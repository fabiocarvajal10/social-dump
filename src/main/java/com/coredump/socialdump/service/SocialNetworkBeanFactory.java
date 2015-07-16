package com.coredump.socialdump.service;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * Created by Franz on 16/07/2015.
 */
@Service
public class SocialNetworkBeanFactory {

  @Inject
  private ApplicationContext applicationContext;

  public FetchableInterface getSocialNetworkFetch(String qualifier) {
    return (FetchableInterface) applicationContext.getBean(qualifier);
  }
}
