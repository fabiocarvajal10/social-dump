package com.coredump.socialdump.service;

import com.google.common.collect.Iterables;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.SocialNetwork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  //@Inject
  //private ScheduledExecutorFactoryBean scheduledExecutorFactoryBean;
  @Inject
  private FetchableInterface twitterFetch;

  public void scheduleFetch(Event event) {
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
    // = new TwitterFetch(Iterables.get(event.getSearchCriteriasById(), 0));
    //scheduledExecutorFactoryBean.setScheduledExecutorTasks(twitterFetch.);
      //twitterFetch.setSearchCriteria(null);
      twitterFetch.setSearchCriteria(Iterables.get(event.getSearchCriteriasById(), 0));
      //twitterFetch.fetchPosts();

    try {
      System.out.println("Date...");
      //Date date = new Date();
      //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
     // String formattedDate = sdf.format(date);
    //  System.out.println(formattedDate);
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

    return new Long(1);
  }

}
