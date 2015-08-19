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


/**
 * Created by Franz on 13/07/2015.
 */
@Service("twitter")
@Scope("prototype")
public class TwitterFetch extends SocialNetworkFetch {

  private final Logger log = LoggerFactory.getLogger(TwitterFetch.class);

  public TwitterFetch() {
    super();
  }

  /**
   * Método que ejecuta el hilo.
   */
  @Override
  public void run() {
    String appId = getSocialNetworkApiCredential().getAppId();
    String appSecret = getSocialNetworkApiCredential().getAppSecret();
    TwitterTemplate twitterTemplate = new TwitterTemplate(appId, appSecret);
    List<SocialNetworkPost> postsList = new ArrayList<>();
    Thread.currentThread().setName(this.getName());
    while (this.getIsAlive()) {
      try {
        log.debug("Obteniendo Tweets de: {}...", getSearchCriteria().getSearchCriteria());
        SearchResults searchResults = twitterTemplate.searchOperations()
            .search(getSearchCriteria().getSearchCriteria());
        List<Tweet> tweetsList = searchResults.getTweets();

        log.debug("Cantidad de Tweets obtenidos: {}...", tweetsList.size());

        tweetsList.forEach(post -> postsList.add(processTweet(post)));

        log.debug("Guardando los Tweets obtenidos");
        getSocialNetworkPostRepository().save(postsList);
        super.notifyPublications(postsList);
        postsList.clear();
        log.debug("Sleeping for " + this.getDelay() + " ms");
        Thread.sleep(this.getDelay());

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

  /**
   * Método que convierte un tweet a un SocialNetworkPost.
   * @param tweet post extraído de la red social
   * @return el post procesado
   */
  private SocialNetworkPost processTweet(Tweet tweet) {
    log.debug("Procesando tweets");
    SocialNetworkPost post = new SocialNetworkPost();
    post.setBody(tweet.getText());
    post.setCreatedAt(new Timestamp(tweet.getCreatedAt().getTime()));
    post.setSnUserId(tweet.getUser().getId());
    post.setMediaUrl(tweet.getSource());
    post.setSnUserEmail(tweet.getUser().getScreenName());
    post.setFullName(tweet.getUser().getName());
    post.setProfileImage(tweet.getUser().getProfileImageUrl());
    post.setProfileUrl(tweet.getUser().getProfileUrl());

    post.setEventByEventId(getSearchCriteria().getEventByEventId());
    post.setGenericStatusByStatusId(getSearchCriteria().getGenericStatusByStatusId());
    post.setSearchCriteriaBySearchCriteriaId(getSearchCriteria());
    post.setSocialNetworkBySocialNetworkId(getSearchCriteria().getSocialNetworkBySocialNetworkId());

    return post;
  }
}
