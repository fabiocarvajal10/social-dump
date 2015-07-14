package com.coredump.socialdump.service;

import com.google.common.collect.Iterables;

import com.coredump.socialdump.domain.Event;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by Franz on 13/07/2015.
 */
@Service
public class FetchExecutorService {

  public FetchExecutorService() {

  }

  public void scheduleFetch(Event event) {
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
    TwitterFetch twitterFetch = new TwitterFetch(Iterables.get(event.getSearchCriteriasById(), 0));

    try {
      System.out.println("Date...");
      Date date = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
      String formattedDate = sdf.format(date);
      System.out.println(formattedDate);
      scheduledExecutorService.schedule(twitterFetch, 5, TimeUnit.SECONDS);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private long getEventStartDelay(Timestamp startDate) {
    Date now = new Date();

    long millisecondsSd = startDate.getTime();
    long millisecondsNow = now.getTime();

    long diff = millisecondsSd - millisecondsNow;
    long diffMinutes = diff / (60 * 1000);

    return diffMinutes;
  }

}
