package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.SocialNetworkPost;
import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Franz on 16/07/2015.
 */
@Service("instagram")
@Scope("prototype")
public class InstagramFetch extends SocialNetworkFetch {

  private static final String ACCESS_TOKEN = "1339991245.964ebd3.d5494e068a314378a11a6c10e3860e99";

  private final Logger log = LoggerFactory.getLogger(InstagramFetch.class);

  private Instagram instagram;

  public InstagramFetch() {
    super();
  }

  @Override
  public void run() {
    Token accessToken =
      new Token(ACCESS_TOKEN, getSocialNetworkApiCredential().getAppSecret());
    instagram = new Instagram(accessToken);
    List<SocialNetworkPost> postsList = new ArrayList<>();
    Thread.currentThread().setName(this.getName());
    this.activateEvent();
    while (this.getIsAlive() && this.isEventActive()) {
      try {
        log.debug("Obteniendo grams de: {}...", getSearchCriteria().getSearchCriteria());
        TagMediaFeed mediaFeed =
          instagram.getRecentMediaTags(getSearchCriteria().getSearchCriteria());
        List<MediaFeedData> mediaFeeds = mediaFeed.getData();

        log.debug("Cantidad de posts (inst) obtenidos: {}...", mediaFeeds.size());

        mediaFeeds.forEach(post -> postsList.add(processGram(post)));

        log.debug("Guardando los grams obtenidos");
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

  private SocialNetworkPost processGram(MediaFeedData mediaFeedData) {
    SocialNetworkPost post = new SocialNetworkPost();
    log.debug("Procesando gram  ");
    post.setBody(mediaFeedData.getCaption().getText());
    post.setCreatedAt(new Timestamp(new Date().getTime()));
    post.setSnUserId(Long.parseLong(mediaFeedData.getUser().getId()));
    post.setMediaUrl(mediaFeedData.getLink());
    post.setProfileImage(mediaFeedData.getUser().getProfilePictureUrl());
    post.setFullName(mediaFeedData.getUser().getFullName());
    post.setProfileUrl(mediaFeedData.getUser().getWebsiteUrl());
    post.setSnUserEmail(mediaFeedData.getUser().getUserName());
    post.setEventByEventId(getSearchCriteria().getEventByEventId());
    post.setGenericStatusByStatusId(getSearchCriteria().getGenericStatusByStatusId());
    post.setSearchCriteriaBySearchCriteriaId(getSearchCriteria());
    post.setSocialNetworkBySocialNetworkId(getSearchCriteria().getSocialNetworkBySocialNetworkId());

    return post;
  }

}
