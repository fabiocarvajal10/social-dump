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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.inject.Inject;

/**
 * Created by Franz on 13/07/2015.
 */
@Service
public class FetchExecutorService {

  private final Logger log = LoggerFactory.getLogger(FetchExecutorService.class);

  @Inject
  private SocialNetworkBeanFactory socialNetworkFetchFactory;

  private HashMap<String, FetchableInterface> fetchableMap = new HashMap<>();

  private ScheduledExecutorService scheduledExecutorService;

  public void scheduleFetch(Event event) {
    int searchCriteriaQ = event.getSearchCriteriasById().size();

    scheduledExecutorService = Executors
          .newScheduledThreadPool(searchCriteriaQ);

    List<SearchCriteria> scList = (List) event.getSearchCriteriasById();

    for (int i = 0; i < searchCriteriaQ; i++) {
      try {
        SearchCriteria searchCriteria = scList.get(i);
        FetchableInterface socialNetworkFetch = socialNetworkFetchFactory
              .getSocialNetworkFetch(searchCriteria
                    .getSocialNetworkBySocialNetworkId()
                    .getName().toLowerCase());

        log.debug("Search Criteria {}", searchCriteria.getSearchCriteria());

        socialNetworkFetch.prepareFetch(searchCriteria, event.getPostDelay());
        addSchedule(socialNetworkFetch, event.getStartDate());
        addToMap(event, socialNetworkFetch, searchCriteria);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void addSchedule(FetchableInterface socialNetworkFetch, DateTime startDate) {
    log.debug("Scheduling {}", getEventStartDelay(startDate));
    scheduledExecutorService.schedule(socialNetworkFetch, 1, TimeUnit.SECONDS);
  }

  private void addToMap(Event event, FetchableInterface socialNetworkFetch,
      SearchCriteria searchCriteria) {

    String key = buildKey(event, searchCriteria);
    fetchableMap.put(key, socialNetworkFetch);

  }

  public boolean stopSynchronization(Event event, SearchCriteria searchCriteria) {
    String key = buildKey(event, searchCriteria);

    if (fetchableMap.containsKey(key) && fetchableMap.get(key) != null) {
      fetchableMap.get(key).kill();
      return true;
    } else {
      return false;
    }

  }

  public boolean modifyDelay(Event event, SearchCriteria searchCriteria, int delay) {
    String key = buildKey(event, searchCriteria);

    if (fetchableMap.containsKey(key) && fetchableMap.get(key) != null) {
      fetchableMap.get(key).setDelay(delay);
      return true;
    } else {
      return false;
    }

  }

  private long getEventStartDelay(DateTime startDate) {
    DateTime now = DateTime.now();
    Minutes diffMinutes = Minutes.minutesBetween(now, startDate);

    return diffMinutes.getMinutes();
  }

  private String buildKey(Event event, SearchCriteria searchCriteria) {
    String key = event.getId().toString()
        + event.getOrganizationByOrganizationId().getId().toString()
        + searchCriteria.getSocialNetworkBySocialNetworkId().getName();

    return key;
  }
}
