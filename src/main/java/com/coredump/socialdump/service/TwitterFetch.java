package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.domain.SocialNetwork;
import com.coredump.socialdump.domain.SocialNetworkPost;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.repository.SearchCriteriaRepository;
import com.coredump.socialdump.repository.SocialNetworkApiCredentialRepository;
import com.coredump.socialdump.repository.SocialNetworkPostRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.jpa.repository.JpaRepository;
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
public class TwitterFetch extends SocialNetworkFetch {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  @Inject
  private SocialNetworkPostRepository socialNetworkPostRepository;

  @Inject
  private SocialNetworkApiCredentialRepository socialNetworkApiCredentialRepository;

  //quitar las keys
  private TwitterTemplate twitterTemplate;

  public TwitterFetch() {
    super(null);
  }

  public TwitterFetch(SearchCriteria searchCriteria) {
    super(searchCriteria);
  }

  @Override
  public void run() {
    twitterTemplate = new TwitterTemplate("AnU9qjdTGUCEdI0QPWIteheFj",
          "UfRZCSnerq9pzIQ74g6AGX3l1LCAeEjeyV7dobYkUpQim5xLDi");
    String searchCriteria = getSearchCriteria().getSearchCriteria();
    List<SocialNetworkPost> postsList = new ArrayList();

    while (true) {
      try {
        SearchResults searchResults = twitterTemplate.searchOperations()
            .search(searchCriteria);
        List<Tweet> tweetsList = searchResults.getTweets();

        for (int i = 0; i < tweetsList.size(); i++) {
          //log.debug("Post {}", processTweet(tweetsList.get(i)).getBody());
          postsList.add(processTweet(tweetsList.get(i)));
        }

//        socialNetworkPostRepository.save(postsList);
        postsList.clear();
        Thread.sleep(4000);

      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  private SocialNetworkPost processTweet(Tweet tweet) {
    SocialNetworkPost post = new SocialNetworkPost();
    post.setBody(tweet.getText());
    post.setCreatedAt(new Timestamp(new Date().getTime()));
    post.setMediaUrl("");
    post.setSnUserEmail(tweet.getUser().getScreenName());
    post.setEventByEventId(getSearchCriteria().getEventByEventId());
    post.setGenericStatusByStatusId(getSearchCriteria().getGenericStatusByStatusId());
    post.setSearchCriteriaBySearchCriteriaId(getSearchCriteria());
    post.setSocialNetworkBySocialNetworkId(getSearchCriteria()
        .getSocialNetworkBySocialNetworkId());
    post.setSocialNetworkRequestByRequestId(null);
    post.setSocialNetworkResponseByResponseId(null);

    return post;
  }

}
