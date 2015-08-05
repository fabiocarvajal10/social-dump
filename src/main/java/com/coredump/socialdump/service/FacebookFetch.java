package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.SocialNetworkPost;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
// import org.springframework.social.facebook.api.Facebook;
// import org.springframework.social.facebook.api.impl.FacebookTemplate;
// import org.springframework.social.facebook.api.PagedList;
// import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@Service("facebook")
@Scope("prototype")
public class FacebookFetch extends SocialNetworkFetch {

  private final Logger log = LoggerFactory.getLogger(FacebookFetch.class);

  private FacebookClient facebookClient;
  // private FacebookTemplate facebookTemplate;   

  public FacebookFetch() {
    super();
  }

  @Override
  public void run() {
    facebookClient = new DefaultFacebookClient(
      getSocialNetworkApiCredential().getAccessToken,
      getSocialNetworkApiCredential().getAppSecret);
    // facebookTemplate = new FacebookTemplate(
    //   getSocialNetworkApiCredential().getAccessToken);
    List<SocialNetworkPost> postsList = new ArrayList<>();
    String searchCriteria = getSearchCriteria().getSearchCriteria();
    while (true) {
      try {
        log.debug("Obteniendo Posts(FB) de: {}...",
          getSearchCriteria().getSearchCriteria());
        // SearchResults searchResults = facebookTemplate.searchOperations()
        //     .search(getSearchCriteria().getSearchCriteria());
        // List<Post> fbPosts = searchResults.getTweets();
        Connection<Post> publicSearch =
          facebookClient.fetchConnection("search", Post.class,
            Parameter.with("q", searchCriteria),
            Parameter.with("type", "post"));


        log.debug("Cantidad de Posts(FB) obtenidos: {}...",
          publicSearch.size());

        fbPosts.forEach(post -> postsList.add(processPost(post)));

        log.debug("Guardando los Posts(FB) obtenidos");
        getSocialNetworkPostRepository().save(postsList);
        super.notifyPublications(postsList);
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

  private SocialNetworkPost processPost(Post fbPost) {
    log.debug("Procesando fbPosts");
    SocialNetworkPost post = new SocialNetworkPost();
    post.setBody(fbPost.getText());
    post.setCreatedAt(new Timestamp(fbPost.getCreatedAt().getTime()));
    post.setSnUserId(fbPost.getUser().getId());
    post.setMediaUrl(fbPost.getSource());
    post.setSnUserEmail(fbPost.getUser().getScreenName());
    post.setFullName(fbPost.getUser().getName());
    post.setProfileImage(fbPost.getUser().getProfileImageUrl());
    post.setProfileUrl(fbPost.getUser().getProfileUrl());

    post.setEventByEventId(getSearchCriteria().getEventByEventId());
    post.setGenericStatusByStatusId(
      getSearchCriteria().getGenericStatusByStatusId());
    post.setSearchCriteriaBySearchCriteriaId(getSearchCriteria());
    post.setSocialNetworkBySocialNetworkId(
      getSearchCriteria().getSocialNetworkBySocialNetworkId());

    return post;
  }
}
