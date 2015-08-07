package com.coredump.socialdump.web.rest;

import com.coredump.socialdump.Application;
import com.coredump.socialdump.domain.SocialNetwork;
import com.coredump.socialdump.repository.SocialNetworkRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        SocialNetworkResource socialNetworkResource = new SocialNetworkResource();
        ReflectionTestUtils.setField(socialNetworkResource, "socialNetworkRepository", socialNetworkRepository);
        this.restSocialNetworkMockMvc = MockMvcBuilders.standaloneSetup(socialNetworkResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        socialNetwork = new SocialNetwork();
        socialNetwork.setName(DEFAULT_NAME);
        socialNetwork.setUrl(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createSocialNetwork() throws Exception {
        int databaseSizeBeforeCreate = socialNetworkRepository.findAll().size();

        // Create the SocialNetwork

        restSocialNetworkMockMvc.perform(post("/api/socialNetworks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(socialNetwork)))
                .andExpect(status().isCreated());

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworks = socialNetworkRepository.findAll();
        assertThat(socialNetworks).hasSize(databaseSizeBeforeCreate + 1);
        SocialNetwork testSocialNetwork = socialNetworks.get(socialNetworks.size() - 1);
        assertThat(testSocialNetwork.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSocialNetwork.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetworkRepository.findAll().size();
        // set the field null
        socialNetwork.setName(null);

        // Create the SocialNetwork, which fails.

        restSocialNetworkMockMvc.perform(post("/api/socialNetworks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(socialNetwork)))
                .andExpect(status().isBadRequest());

        List<SocialNetwork> socialNetworks = socialNetworkRepository.findAll();
        assertThat(socialNetworks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetworkRepository.findAll().size();
        // set the field null
        socialNetwork.setUrl(null);

        // Create the SocialNetwork, which fails.

        restSocialNetworkMockMvc.perform(post("/api/socialNetworks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(socialNetwork)))
                .andExpect(status().isBadRequest());

        List<SocialNetwork> socialNetworks = socialNetworkRepository.findAll();
        assertThat(socialNetworks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSocialNetworks() throws Exception {
        // Initialize the database
        socialNetworkRepository.saveAndFlush(socialNetwork);

        // Get all the socialNetworks
        restSocialNetworkMockMvc.perform(get("/api/socialNetworks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(socialNetwork.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void getSocialNetwork() throws Exception {
        // Initialize the database
        socialNetworkRepository.saveAndFlush(socialNetwork);

        // Get the socialNetwork
        restSocialNetworkMockMvc.perform(get("/api/socialNetworks/{id}", socialNetwork.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(socialNetwork.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSocialNetwork() throws Exception {
        // Get the socialNetwork
        restSocialNetworkMockMvc.perform(get("/api/socialNetworks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSocialNetwork() throws Exception {
        // Initialize the database
        socialNetworkRepository.saveAndFlush(socialNetwork);

		int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().size();

        // Update the socialNetwork
        socialNetwork.setName(UPDATED_NAME);
        socialNetwork.setUrl(UPDATED_URL);
        

        restSocialNetworkMockMvc.perform(put("/api/socialNetworks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(socialNetwork)))
                .andExpect(status().isOk());

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworks = socialNetworkRepository.findAll();
        assertThat(socialNetworks).hasSize(databaseSizeBeforeUpdate);
        SocialNetwork testSocialNetwork = socialNetworks.get(socialNetworks.size() - 1);
        assertThat(testSocialNetwork.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSocialNetwork.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void deleteSocialNetwork() throws Exception {
        // Initialize the database
        socialNetworkRepository.saveAndFlush(socialNetwork);

		int databaseSizeBeforeDelete = socialNetworkRepository.findAll().size();

        // Get the socialNetwork
        restSocialNetworkMockMvc.perform(delete("/api/socialNetworks/{id}", socialNetwork.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SocialNetwork> socialNetworks = socialNetworkRepository.findAll();
        assertThat(socialNetworks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
