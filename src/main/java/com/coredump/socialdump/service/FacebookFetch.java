package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.SocialNetworkPost;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.Post;

import com.restfb.types.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Recolector de Posts de Facebook.
 */
@Service("facebook")
@Scope("prototype")
public class FacebookFetch extends SocialNetworkFetch {

  /**
   * Escritor de bitácoras.
   */
  private final Logger log = LoggerFactory.getLogger(FacebookFetch.class);

  /**
   * Hashtag para buscar en Facebook
   */
  private String hashtag;

  /**
   * Interfaz para obtener Posts de Facebook.
   */
  private FacebookClient facebookClient;
  // private FacebookTemplate facebookTemplate;

  /**
   * Constructor por defecto
   */
  public FacebookFetch() {
    super();
  }

  public void init() {
    facebookClient = new DefaultFacebookClient(
      getSocialNetworkApiCredential().getAccessToken(), Version.VERSION_2_4);
    System.err.println("ACCESS TOKEN: " +
      getSocialNetworkApiCredential().getAccessToken());
  }

  /**
   * Ejecuta el proceso de obtener Posts de Facebook
   */
  @Override
  public void run() {
    init();
    List<SocialNetworkPost> posts = new ArrayList<>();
    String hashtag = getHasthag();
    while (true) {
      try {
        posts = fetch();
        getSocialNetworkPostRepository().save(posts);
        super.notifyPublications(posts);
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

  /**
   * Obtiene posts de Facebook
   * @return posts de Facebook
   */
  protected List<SocialNetworkPost> fetch() {
    ArrayList<SocialNetworkPost> posts = new ArrayList<>();
    log.debug("Obteniendo Posts(FB) de: {}...", this.getHasthag());
    Connection<Post> publicSearch =
      facebookClient.fetchConnection("search", Post.class,
        Parameter.with("q", this.hashtag),
        Parameter.with("type", "post"));

    log.debug("Cantidad de Posts(FB) obtenidos: {}...",
      publicSearch.getData().size());

    publicSearch.getData().forEach(
      post -> posts.add(processPost(post)));

    log.debug("Guardando los Posts(FB) obtenidos");
    return posts;
  }

  /**
   * Procesa un Post de Facebook convirtiéndolo en uno de la aplicación.
   * @param fbPost Post de Facebook
   * @return post de la aplicación
   */
  private SocialNetworkPost processPost(Post fbPost) {
    log.debug("Procesando fbPosts");
    SocialNetworkPost post = new SocialNetworkPost();
    post.setBody(fbPost.getMessage());
    post.setCreatedAt(new Timestamp(fbPost.getCreatedTime().getTime()));
    post.setSnUserId(Long.parseLong(fbPost.getFrom().getId()));
    post.setMediaUrl(fbPost.getSource());

    post.setEventByEventId(getSearchCriteria().getEventByEventId());
    post.setGenericStatusByStatusId(
      getSearchCriteria().getGenericStatusByStatusId());
    post.setSearchCriteriaBySearchCriteriaId(getSearchCriteria());
    post.setSocialNetworkBySocialNetworkId(
      getSearchCriteria().getSocialNetworkBySocialNetworkId());

    try {
      User user = fetchUser(fbPost.getFrom().getId());
      post.setSnUserEmail(user.getEmail());
      post.setFullName(String.format("%s %s %s",
        user.getFirstName(), user.getMiddleName(), user.getLastName()));
      post.setProfileImage(user.getPicture().getUrl());
      post.setProfileUrl(user.getLink());
    } catch (Exception e) {
      post.setSnUserEmail(fbPost.getFrom().getName());
      post.setFullName(fbPost.getFrom().getName());
    }

    return post;
  }

  /**
   * Obtiene un usuario de Facebook
   * @param userId id del usuario en Facebook
   * @return usuario de Facebook
   */
  private User fetchUser(String userId) {
    return facebookClient.fetchObject(userId, User.class);
  }

  /**
   * Genera un Hashtag a partir de un texto.
   * @param qParam texto
   * @return Hashtag
   */
  protected String hashTagFromText(String qParam) {
    String hashtag;
    if((qParam == null) || (qParam.length() == 0)) {
      return "";
    }
//    try {
      hashtag = "#" + qParam.replaceAll("^#+", "");
      // hashtag = URLEncoder.encode("#" + qParam.replaceAll("^#+", ""), "UTF-8");
//    } catch (UnsupportedEncodingException e) {
//      hashtag = qParam;
//    }
    return hashtag;
  }

  /**
   * Obtiene el Hashtag
   * @return hashtag
   */
  public String getHasthag() {
    if(hashtag != null && hashtag.length() != 0)
      return hashtag;
    if (getSearchCriteria() == null)
      return "";
    hashtag = hashTagFromText(getSearchCriteria().getSearchCriteria());
    return hashtag;
  }

  /**
   * Asigna el hashtag
   * @param hasthag hasthag
   */
  public void setHasthag(String hasthag) {
    this.hashtag = hasthag;
  }
}

