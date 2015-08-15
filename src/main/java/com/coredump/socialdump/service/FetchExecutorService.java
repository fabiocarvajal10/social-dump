package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.SearchCriteria;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by Francisco J. Milanés Sánchez on 13/07/2015.
 */
@Service
public class FetchExecutorService {

  private final Logger log = LoggerFactory.getLogger(FetchExecutorService.class);

  @Inject
  private SocialNetworkBeanFactory socialNetworkFetchFactory;

  private Map<String, FetchableInterface> fetchableMap = new HashMap<>();

  private ScheduledExecutorService scheduledExecutorService;

  public void scheduleFetch(Event event) {
    int searchCriteriaQ = event.getSearchCriteriasById().size();

    scheduledExecutorService = Executors
          .newScheduledThreadPool(searchCriteriaQ);

    List<SearchCriteria> scList = (List) event.getSearchCriteriasById();

    scList.forEach(sc -> {
        try {
          FetchableInterface socialNetworkFetch = socialNetworkFetchFactory
              .getSocialNetworkFetch(sc.getSocialNetworkBySocialNetworkId()
              .getName().toLowerCase());

          log.debug("Search Criteria {}", sc.getSearchCriteria());
          socialNetworkFetch.prepareFetch(sc, event.getPostDelay());
          addSchedule(socialNetworkFetch, event.getStartDate());
          addToMap(event, socialNetworkFetch, sc);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
  }

  private void addSchedule(FetchableInterface socialNetworkFetch, DateTime startDate) {
    log.debug("Scheduled to start in {} mins", getEventStartDelay(startDate));
    scheduledExecutorService
      .schedule(socialNetworkFetch, getEventStartDelay(startDate), TimeUnit.MINUTES);
  }

  private void addToMap(Event event, FetchableInterface socialNetworkFetch,
      SearchCriteria searchCriteria) {
    String key = buildKey(event, searchCriteria);
    fetchableMap.put(key, socialNetworkFetch);
  }

  public boolean stopSynchronization(SearchCriteria searchCriteria) {
    String key = buildKey(searchCriteria.getEventByEventId(), searchCriteria);

    if (fetchableMap.containsKey(key) && fetchableMap.get(key) != null) {
      fetchableMap.get(key).kill();
      return true;
    } else {
      return false;
    }

  }

  public void killAll(Event event) {
    event.getSearchCriteriasById()
      .forEach(sc -> fetchableMap.get(buildKey(event, sc)).kill());
  }

  public boolean modifyDelay(SearchCriteria searchCriteria, int delay) {
    String key = buildKey(searchCriteria.getEventByEventId(), searchCriteria);

    if (fetchableMap.containsKey(key) && fetchableMap.get(key) != null) {
      fetchableMap.get(key).setDelay(delay);
      return true;
    } else {
      return false;
    }

  }

  public void delayAll(Event event, int delay) {
    event.getSearchCriteriasById()
      .forEach(sc -> fetchableMap.get(buildKey(event, sc)).setDelay(delay));
  }

  private long getEventStartDelay(DateTime startDate) {
    DateTime now = new DateTime();
    return Minutes.minutesBetween(now, startDate).getMinutes();
  }

  private String buildKey(Event event, SearchCriteria searchCriteria) {
    String key = event.getId().toString()
        + event.getOrganizationByOrganizationId().getId().toString()
        + searchCriteria.getSocialNetworkBySocialNetworkId().getName();

    return key;
  }
}
