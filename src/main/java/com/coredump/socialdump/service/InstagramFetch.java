package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.SocialNetwork;
import com.coredump.socialdump.domain.SocialNetworkPost;

import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.print.DocFlavor;


/**
 * Created by Franz on 16/07/2015.
 */
@Service("instagram")
@Scope("prototype")
public class InstagramFetch extends SocialNetworkFetch {

  private final String ACCESS_TOKEN = "1339991245.964ebd3.d5494e068a314378a11a6c10e3860e99";

  private final Logger log = LoggerFactory.getLogger(InstagramFetch.class);

  private Instagram instagram;
    private InstagramService instagramService;

    public InstagramFetch() {
    super();
  }

  @Override
  public void run() {
    Token accessToken =
        new Token(ACCESS_TOKEN, getSocialNetworkApiCredential().getAppSecret());
    instagram = new Instagram(accessToken);
    List<SocialNetworkPost> postsList = new ArrayList<>();
      while (true) {
          try {
              log.debug("Obteniendo grams de: {}...", getSearchCriteria().getSearchCriteria());
              TagMediaFeed mediaFeed =
                  instagram.getRecentMediaTags(getSearchCriteria().getSearchCriteria());
              List<MediaFeedData> mediaFeeds = mediaFeed.getData();

              log.debug("Cantidad de posts (inst) obtenidos: {}...", mediaFeeds.size());
              for (int i = 0; i < mediaFeeds.size(); i++) {
                  postsList.add(processGram(mediaFeeds.get(i)));
              }

              //log.debug("Guardando los grams obtenidos");
              getSocialNetworkPostRepository().save(postsList);
              postsList.clear();
              log.debug("Sleeping");
              Thread.sleep(10000);

          } catch (Exception e) {
              e.printStackTrace();
          }
      }
  }

  private SocialNetworkPost processGram(MediaFeedData mediaFeedData) {
    SocialNetworkPost post = new SocialNetworkPost();

    post.setBody(mediaFeedData.getCaption().getText().replaceAll("[^\\x20-\\x7e]", ""));
    post.setCreatedAt(new Timestamp(new Date().getTime()));
    post.setSnUserId(new Long(1));
    post.setMediaUrl(mediaFeedData.getLink());
    post.setSnUserEmail(mediaFeedData.getUser().getUserName());
    post.setEventByEventId(getSearchCriteria().getEventByEventId());
    post.setGenericStatusByStatusId(getSearchCriteria().getGenericStatusByStatusId());
    post.setSearchCriteriaBySearchCriteriaId(getSearchCriteria());
    post.setSocialNetworkBySocialNetworkId(getSearchCriteria().getSocialNetworkBySocialNetworkId());

    return post;
  }

}
