package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.SocialNetworkPost;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Franz on 13/07/2015.
 */
@Service("twitter")
@Scope("prototype")
public class TwitterFetch extends SocialNetworkFetch {

  private final Logger log = LoggerFactory.getLogger(TwitterFetch.class);

  private TwitterTemplate twitterTemplate;

  public TwitterFetch() {
    super();
  }

  @Override
  public void run() {
    String appId = getSocialNetworkApiCredential().getAppId();
    String appSecret = getSocialNetworkApiCredential().getAppSecret();
    twitterTemplate = new TwitterTemplate(appId, appSecret);
    List<SocialNetworkPost> postsList = new ArrayList<>();
    while (true) {
      try {
        log.debug("Obteniendo Tweets de: {}...", getSearchCriteria().getSearchCriteria());
        SearchResults searchResults = twitterTemplate.searchOperations()
            .search(getSearchCriteria().getSearchCriteria());
        List<Tweet> tweetsList = searchResults.getTweets();

        log.debug("Cantidad de Tweets obtenidos: {}...", tweetsList.size());
        tweetsList.forEach(post -> postsList.add(processTweet(post)));

        log.debug("Guardando los Tweets obtenidos");
        getSocialNetworkPostRepository().save(postsList);
        postsList.clear();
        log.debug("Sleeping");
        Thread.sleep(10000);

      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
        try {
          Thread.sleep(30000);
        } catch (InterruptedException ex) {
          log.debug("Exception putting thread to sleep");
          ex.printStackTrace();
        }
      }
    }
  }

  private SocialNetworkPost processTweet(Tweet tweet) {

    SocialNetworkPost post = new SocialNetworkPost();
    post.setBody(tweet.getText().replaceAll("[^\\x20-\\x7e]", ""));
    post.setCreatedAt(new Timestamp(tweet.getCreatedAt().getTime()));
    post.setSnUserId(tweet.getUser().getId());
    post.setMediaUrl(tweet.getSource());
    post.setSnUserEmail(tweet.getUser().getScreenName());
    post.setEventByEventId(getSearchCriteria().getEventByEventId());
    post.setGenericStatusByStatusId(getSearchCriteria().getGenericStatusByStatusId());
    post.setSearchCriteriaBySearchCriteriaId(getSearchCriteria());
    post.setSocialNetworkBySocialNetworkId(getSearchCriteria().getSocialNetworkBySocialNetworkId());

    return post;
  }
}
