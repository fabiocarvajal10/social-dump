package com.coredump.socialdump.service;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * Created by Francisco Milanés Sánchez on 16/07/2015.
 */
@Service
public class SocialNetworkBeanFactory {

  @Inject
  private ApplicationContext applicationContext;

  /**
   * Retorna un bean de tipo FetchableInterface
   * @param name nombre del tipo que se quiere obtener
   * @return FetchableInterface
   */
  public FetchableInterface getSocialNetworkFetch(String name) {
    return (FetchableInterface) applicationContext.getBean(name);
  }
}
