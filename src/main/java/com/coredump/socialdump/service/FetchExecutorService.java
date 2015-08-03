package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.SearchCriteria;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by Franz on 13/07/2015.
 */
@Service
public class FetchExecutorService {

  private final Logger log = LoggerFactory.getLogger(FetchExecutorService.class);

  @Inject
  private SocialNetworkBeanFactory socialNetworkFetchFactory;

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

        socialNetworkFetch.setSearchCriteria(scList.get(i));
        addSchedule(socialNetworkFetch, event.getStartDate());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void addSchedule(FetchableInterface socialNetworkFetch, DateTime startDate) {
    log.debug("Scheduling {}", getEventStartDelay(startDate));
    scheduledExecutorService.schedule(socialNetworkFetch, 1, TimeUnit.SECONDS);
  }

  private long getEventStartDelay(DateTime startDate) {
    DateTime now = DateTime.now();
    Minutes diffMinutes = Minutes.minutesBetween(now, startDate);

    return diffMinutes.getMinutes();
  }

}
