package com.coredump.socialdump.web.rest;

import com.coredump.socialdump.Application;
import com.coredump.socialdump.domain.GenericStatus;
import com.coredump.socialdump.domain.SocialNetwork;
import com.coredump.socialdump.repository.SocialNetworkRepository;
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

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SocialNetworkResource REST controller.
 *
 * @see SocialNetworkResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SocialNetworkResourceTest {

  private static final String DEFAULT_NAME = "SAMPLE_TEXT";
  private static final String UPDATED_NAME = "UPDATED_TEXT";
  private static final String DEFAULT_URL = "SAMPLE_TEXT";
  private static final String UPDATED_URL = "UPDATED_TEXT";

  @Inject
  private SocialNetworkRepository socialNetworkRepository;

  @Inject
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  private MockMvc restSocialNetworkMockMvc;

  private SocialNetwork socialNetwork;

  @PostConstruct
  public void setup() {
    MockitoAnnotations.initMocks(this);
    SocialNetworkResource socialNetworkResource =
      new SocialNetworkResource();
    ReflectionTestUtils.setField(socialNetworkResource,
      "socialNetworkRepository", socialNetworkRepository);
    this.restSocialNetworkMockMvc =
      MockMvcBuilders.standaloneSetup(socialNetworkResource)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    socialNetwork = new SocialNetwork();
    socialNetwork.setName(DEFAULT_NAME);
    socialNetwork.setUrl(DEFAULT_URL);
    GenericStatus gs = new GenericStatus();
    gs.setId((short) 1);
    socialNetwork.setGenericStatusByStatusId(gs);
  }

  @Test
  @Transactional
  public void getAllSocialNetworks() throws Exception {
    // Initialize the database
    socialNetworkRepository.saveAndFlush(socialNetwork);

    // Get all the socialNetworks
    restSocialNetworkMockMvc.perform(get("/api/social-networks"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.[*].id").value(hasItem(socialNetwork.getId())))
      .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
      .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
  }

  @Test
  @Transactional
  public void getSocialNetwork() throws Exception {
    // Initialize the database
    socialNetworkRepository.saveAndFlush(socialNetwork);

    // Get the socialNetwork
    restSocialNetworkMockMvc.perform(get("/api/social-networks/{id}", socialNetwork.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.id").value(socialNetwork.getId()))
      .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
      .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingSocialNetwork() throws Exception {
    // Get the socialNetwork
    restSocialNetworkMockMvc.perform(get("/api/social-networks/{id}",
      Short.MAX_VALUE)).andExpect(status().isNotFound());
  }

}
