package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.domain.SocialNetwork;
import com.coredump.socialdump.domain.SocialNetworkPost;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.repository.GenericStatusRepository;
import com.coredump.socialdump.repository.ReqRepo;
import com.coredump.socialdump.repository.ResponseRepo;
import com.coredump.socialdump.repository.SearchCriteriaRepository;
import com.coredump.socialdump.repository.SocialNetworkApiCredentialRepository;
import com.coredump.socialdump.repository.SocialNetworkPostRepository;
import com.coredump.socialdump.repository.SocialNetworkRepository;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.SocketOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Franz on 13/07/2015.
 */
@Service
public class TwitterFetch extends SocialNetworkFetch implements FetchableInterface {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private SearchCriteria searchCriteria;

  @Inject
  private SocialNetworkPostRepository socialNetworkPostRepository;

  @Inject
  private SocialNetworkApiCredentialRepository socialNetworkApiCredentialRepository;

  @Inject
  private ReqRepo reqRepo;

  @Inject
  private ResponseRepo responseRepo;

  private TwitterTemplate twitterTemplate;

  public TwitterFetch() {
    //super(null);
  }

  public TwitterFetch(SearchCriteria searchCriteria) {
    //super(searchCriteria);

  }

  public void setSearchCriteria(SearchCriteria searchCriteria) {
    this.searchCriteria = searchCriteria;
  }

  public SearchCriteria getSearchCriteria() {
    return searchCriteria;
  }

  //@AsyncfetchPosts
  @Override
  public void run() {
    String appId = socialNetworkApiCredentialRepository.findOne(new Long(1)).getAppId();
    String appSecret = socialNetworkApiCredentialRepository.findOne(new Long(1)).getAppSecret();
    twitterTemplate = new TwitterTemplate(appId, appSecret);
    List<SocialNetworkPost> postsList = new ArrayList();
    while(true) {
        try {
            SearchResults searchResults = twitterTemplate.searchOperations()
                .search(searchCriteria.getSearchCriteria());
            List<Tweet> tweetsList = searchResults.getTweets();

            for (int i = 0; i < tweetsList.size(); i++) {
                System.out.println(tweetsList.get(1).getText());
                postsList.add(processTweet(tweetsList.get(i)));
            }

            socialNetworkPostRepository.save(postsList);
            postsList.clear();
            System.out.println("Sleeping...");
            Thread.sleep(10000);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
  }

    private SocialNetworkPost processTweet(Tweet tweet) {
        SocialNetworkPost post = new SocialNetworkPost();
        post.setBody(tweet.getText().replaceAll("[^\\x20-\\x7e]", ""));
        post.setCreatedAt(new Timestamp(new Date().getTime()));
        post.setSnUserId(new Long(1));
        post.setMediaUrl("url");
        post.setSnUserEmail(tweet.getUser().getScreenName());
        post.setEventByEventId(getSearchCriteria().getEventByEventId());
        post.setGenericStatusByStatusId(searchCriteria.getGenericStatusByStatusId());
        post.setSearchCriteriaBySearchCriteriaId(getSearchCriteria());
        post.setSocialNetworkBySocialNetworkId(searchCriteria.getSocialNetworkBySocialNetworkId());
        post.setSocialNetworkRequestByRequestId(reqRepo.findOne(new Long(1)));
        post.setSocialNetworkResponseByResponseId(responseRepo.findOne(new Long(1)));

        return post;
    }

}
