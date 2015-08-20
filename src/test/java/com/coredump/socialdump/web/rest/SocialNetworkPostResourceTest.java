package com.coredump.socialdump.web.rest;

import com.coredump.socialdump.Application;
import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.GenericStatus;
import com.coredump.socialdump.domain.SocialNetwork;
import com.coredump.socialdump.domain.SocialNetworkPost;
import com.coredump.socialdump.repository.SocialNetworkPostRepository;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SocialNetworkPostResource REST controller.
 *
 * @see SocialNetworkPostResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SocialNetworkPostResourceTest {

  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


  private static final Timestamp DEFAULT_CREATED_AT = new Timestamp(0L);
  private static final Timestamp UPDATED_CREATED_AT = new Timestamp(System.currentTimeMillis());
  private static final String DEFAULT_CREATED_AT_STR = DEFAULT_CREATED_AT.toString();

  private static final Long DEFAULT_SN_USER_ID = 1L;
  private static final Long UPDATED_SN_USER_ID = 2L;
  private static final String DEFAULT_SN_USER_EMAIL = "SAMPLE_TEXT";
  private static final String UPDATED_SN_USER_EMAIL = "UPDATED_TEXT";
  private static final String DEFAULT_BODY = "SAMPLE_TEXT";
  private static final String UPDATED_BODY = "UPDATED_TEXT";
  private static final String DEFAULT_MEDIA_URL = "SAMPLE_TEXT";
  private static final String UPDATED_MEDIA_URL = "UPDATED_TEXT";
  private static final String DEFAULT_FULL_NAME = "SAMPLE_TEXT";
  private static final String UPDATED_FULL_NAME = "UPDATED_TEXT";
  private static final String DEFAULT_PROFILE_IMAGE = "SAMPLE_TEXT";
  private static final String UPDATED_PROFILE_IMAGE = "UPDATED_TEXT";
  private static final String DEFAULT_PROFILE_URL = "SAMPLE_TEXT";
  private static final String UPDATED_PROFILE_URL = "UPDATED_TEXT";

  @Inject
  private SocialNetworkPostRepository socialNetworkPostRepository;

  @Inject
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  private MockMvc restSocialNetworkPostMockMvc;

  private SocialNetworkPost socialNetworkPost;

  @PostConstruct
  public void setup() {
    MockitoAnnotations.initMocks(this);
    SocialNetworkPostResource socialNetworkPostResource = new SocialNetworkPostResource();
    ReflectionTestUtils.setField(socialNetworkPostResource, "socialNetworkPostRepository", socialNetworkPostRepository);
    this.restSocialNetworkPostMockMvc = MockMvcBuilders.standaloneSetup(socialNetworkPostResource).setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    socialNetworkPost = new SocialNetworkPost();
    socialNetworkPost.setCreatedAt(DEFAULT_CREATED_AT);
    socialNetworkPost.setSnUserId(DEFAULT_SN_USER_ID);
    socialNetworkPost.setSnUserEmail(DEFAULT_SN_USER_EMAIL);
    socialNetworkPost.setBody(DEFAULT_BODY);
    socialNetworkPost.setMediaUrl(DEFAULT_MEDIA_URL);
    socialNetworkPost.setFullName(DEFAULT_FULL_NAME);
    socialNetworkPost.setProfileImage(DEFAULT_PROFILE_IMAGE);
    socialNetworkPost.setProfileUrl(DEFAULT_PROFILE_URL);
    GenericStatus gs = new GenericStatus();
    gs.setId((short) 1);
    socialNetworkPost.setGenericStatusByStatusId(gs);

    SocialNetwork sn = new SocialNetwork();
    sn.setId(1);
    socialNetworkPost.setSocialNetworkBySocialNetworkId(sn);

    Event e = new Event();
    e.setId(1L);
    socialNetworkPost.setEventByEventId(e);
  }

  @Test
  @Transactional
  public void createSocialNetworkPost() throws Exception {
    int databaseSizeBeforeCreate = socialNetworkPostRepository.findAll().size();

    // Create the SocialNetworkPost

    restSocialNetworkPostMockMvc.perform(post("/api/social-network-posts")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(socialNetworkPost)))
      .andExpect(status().isCreated());

    // Validate the SocialNetworkPost in the database
    List<SocialNetworkPost> socialNetworkPosts = socialNetworkPostRepository.findAll();
    assertThat(socialNetworkPosts).hasSize(databaseSizeBeforeCreate + 1);
    SocialNetworkPost testSocialNetworkPost = socialNetworkPosts.get(socialNetworkPosts.size() - 1);
    assertThat(testSocialNetworkPost.getCreatedAt().equals(DEFAULT_CREATED_AT));
    assertThat(testSocialNetworkPost.getSnUserId()).isEqualTo(DEFAULT_SN_USER_ID);
    assertThat(testSocialNetworkPost.getSnUserEmail()).isEqualTo(DEFAULT_SN_USER_EMAIL);
    assertThat(testSocialNetworkPost.getBody()).isEqualTo(DEFAULT_BODY);
    assertThat(testSocialNetworkPost.getMediaUrl()).isEqualTo(DEFAULT_MEDIA_URL);
    assertThat(testSocialNetworkPost.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
    assertThat(testSocialNetworkPost.getProfileImage()).isEqualTo(DEFAULT_PROFILE_IMAGE);
    assertThat(testSocialNetworkPost.getProfileUrl()).isEqualTo(DEFAULT_PROFILE_URL);
  }

  @Test
  @Transactional
  public void checkCreatedAtIsRequired() throws Exception {
    int databaseSizeBeforeTest = socialNetworkPostRepository.findAll().size();
    // set the field null
    socialNetworkPost.setCreatedAt(null);

    // Create the SocialNetworkPost, which fails.

    restSocialNetworkPostMockMvc.perform(post("/api/social-network-posts")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(socialNetworkPost)))
      .andExpect(status().isBadRequest());

    List<SocialNetworkPost> socialNetworkPosts = socialNetworkPostRepository.findAll();
    assertThat(socialNetworkPosts).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllSocialNetworkPosts() throws Exception {
    // Initialize the database
    socialNetworkPostRepository.saveAndFlush(socialNetworkPost);

    // Get all the socialNetworkPosts
    restSocialNetworkPostMockMvc.perform(get("/api/social-network-posts"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.[*].id").value(hasItem(socialNetworkPost.getId())))
      .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT_STR)))
      .andExpect(jsonPath("$.[*].snUserId").value(hasItem(DEFAULT_SN_USER_ID)))
      .andExpect(jsonPath("$.[*].snUserEmail").value(hasItem(DEFAULT_SN_USER_EMAIL.toString())))
      .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
      .andExpect(jsonPath("$.[*].mediaUrl").value(hasItem(DEFAULT_MEDIA_URL.toString())))
      .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME.toString())))
      .andExpect(jsonPath("$.[*].profileImage").value(hasItem(DEFAULT_PROFILE_IMAGE.toString())))
      .andExpect(jsonPath("$.[*].profileUrl").value(hasItem(DEFAULT_PROFILE_URL.toString())));
  }

  @Test
  @Transactional
  public void getSocialNetworkPost() throws Exception {
    // Initialize the database
    socialNetworkPostRepository.saveAndFlush(socialNetworkPost);

    // Get the socialNetworkPost
    restSocialNetworkPostMockMvc.perform(get("/api/social-network-posts/{id}", socialNetworkPost.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.id").value(socialNetworkPost.getId()))
      .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT_STR))
      .andExpect(jsonPath("$.snUserId").value(DEFAULT_SN_USER_ID))
      .andExpect(jsonPath("$.snUserEmail").value(DEFAULT_SN_USER_EMAIL.toString()))
      .andExpect(jsonPath("$.body").value(DEFAULT_BODY.toString()))
      .andExpect(jsonPath("$.mediaUrl").value(DEFAULT_MEDIA_URL.toString()))
      .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME.toString()))
      .andExpect(jsonPath("$.profileImage").value(DEFAULT_PROFILE_IMAGE.toString()))
      .andExpect(jsonPath("$.profileUrl").value(DEFAULT_PROFILE_URL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingSocialNetworkPost() throws Exception {
    // Get the socialNetworkPost
    restSocialNetworkPostMockMvc.perform(get("/api/social-network-posts/{id}", Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateSocialNetworkPost() throws Exception {
    // Initialize the database
    socialNetworkPostRepository.saveAndFlush(socialNetworkPost);

    int databaseSizeBeforeUpdate = socialNetworkPostRepository.findAll().size();

    // Update the socialNetworkPost
    socialNetworkPost.setCreatedAt(UPDATED_CREATED_AT);
    socialNetworkPost.setSnUserId(UPDATED_SN_USER_ID);
    socialNetworkPost.setSnUserEmail(UPDATED_SN_USER_EMAIL);
    socialNetworkPost.setBody(UPDATED_BODY);
    socialNetworkPost.setMediaUrl(UPDATED_MEDIA_URL);
    socialNetworkPost.setFullName(UPDATED_FULL_NAME);
    socialNetworkPost.setProfileImage(UPDATED_PROFILE_IMAGE);
    socialNetworkPost.setProfileUrl(UPDATED_PROFILE_URL);


    restSocialNetworkPostMockMvc.perform(put("/api/social-network-posts")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(socialNetworkPost)))
      .andExpect(status().isOk());

    // Validate the SocialNetworkPost in the database
    List<SocialNetworkPost> socialNetworkPosts = socialNetworkPostRepository.findAll();
    assertThat(socialNetworkPosts).hasSize(databaseSizeBeforeUpdate);
    SocialNetworkPost testSocialNetworkPost = socialNetworkPosts.get(socialNetworkPosts.size() - 1);
    assertThat(testSocialNetworkPost.getCreatedAt().equals(UPDATED_CREATED_AT));
    assertThat(testSocialNetworkPost.getSnUserId()).isEqualTo(UPDATED_SN_USER_ID);
    assertThat(testSocialNetworkPost.getSnUserEmail()).isEqualTo(UPDATED_SN_USER_EMAIL);
    assertThat(testSocialNetworkPost.getBody()).isEqualTo(UPDATED_BODY);
    assertThat(testSocialNetworkPost.getMediaUrl()).isEqualTo(UPDATED_MEDIA_URL);
    assertThat(testSocialNetworkPost.getFullName()).isEqualTo(UPDATED_FULL_NAME);
    assertThat(testSocialNetworkPost.getProfileImage()).isEqualTo(UPDATED_PROFILE_IMAGE);
    assertThat(testSocialNetworkPost.getProfileUrl()).isEqualTo(UPDATED_PROFILE_URL);
  }

  @Test
  @Transactional
  public void deleteSocialNetworkPost() throws Exception {
    // Initialize the database
    socialNetworkPostRepository.saveAndFlush(socialNetworkPost);

    int databaseSizeBeforeDelete = socialNetworkPostRepository.findAll().size();

    // Get the socialNetworkPost
    restSocialNetworkPostMockMvc.perform(delete("/api/social-network-posts/{id}", socialNetworkPost.getId())
      .accept(TestUtil.APPLICATION_JSON_UTF8))
      .andExpect(status().isOk());

    // Validate the database is empty
    List<SocialNetworkPost> socialNetworkPosts = socialNetworkPostRepository.findAll();
    assertThat(socialNetworkPosts).hasSize(databaseSizeBeforeDelete - 1);
  }
}
