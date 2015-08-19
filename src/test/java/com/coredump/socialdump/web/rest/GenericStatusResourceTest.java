package com.coredump.socialdump.web.rest;

import com.coredump.socialdump.Application;
import com.coredump.socialdump.domain.GenericStatus;
import com.coredump.socialdump.repository.GenericStatusRepository;
import com.coredump.socialdump.web.rest.dto.GenericStatusDTO;
import com.coredump.socialdump.web.rest.mapper.GenericStatusMapper;

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
 * Test class for the GenericStatusResource REST controller.
 *
 * @see GenericStatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GenericStatusResourceTest {

  private static final String DEFAULT_STATUS = "SAMPLE_TEXT";
  private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";

  @Inject
  private GenericStatusRepository genericStatusRepository;

  @Inject
  private GenericStatusMapper genericStatusMapper;

  @Inject
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  private MockMvc restGenericStatusMockMvc;

  private GenericStatus genericStatus;

  @PostConstruct
  public void setup() {
    MockitoAnnotations.initMocks(this);
    GenericStatusResource genericStatusResource = new GenericStatusResource();
    ReflectionTestUtils.setField(genericStatusResource,
      "genericStatusRepository", genericStatusRepository);
    ReflectionTestUtils.setField(genericStatusResource,
      "genericStatusMapper", genericStatusMapper);
    this.restGenericStatusMockMvc =
      MockMvcBuilders.standaloneSetup(genericStatusResource)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    genericStatus = new GenericStatus();
    genericStatus.setStatus(DEFAULT_STATUS);
    genericStatus.setDescription(DEFAULT_DESCRIPTION);
  }

  @Test
  @Transactional
  public void createGenericStatus() throws Exception {
    int databaseSizeBeforeCreate = genericStatusRepository.findAll().size();

    // Create the GenericStatus
    GenericStatusDTO genericStatusDTO = genericStatusMapper
      .genericStatusToGenericStatusDTO(genericStatus);

    restGenericStatusMockMvc.perform(post("/api/generic-statuses")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(genericStatusDTO)))
      .andExpect(status().isCreated());

    // Validate the GenericStatus in the database
    List<GenericStatus> genericStatuses = genericStatusRepository.findAll();
    assertThat(genericStatuses).hasSize(databaseSizeBeforeCreate + 1);
    GenericStatus testGenericStatus = genericStatuses.get(genericStatuses.size() - 1);
    assertThat(testGenericStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
    assertThat(testGenericStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
  }

  @Test
  @Transactional
  public void checkStatusIsRequired() throws Exception {
    int databaseSizeBeforeTest = genericStatusRepository.findAll().size();
    // set the field null
    genericStatus.setStatus(null);

    // Create the GenericStatus, which fails.
    GenericStatusDTO genericStatusDTO = genericStatusMapper.genericStatusToGenericStatusDTO(genericStatus);

    restGenericStatusMockMvc.perform(post("/api/generic-statuses")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(genericStatusDTO)))
      .andExpect(status().isBadRequest());

    List<GenericStatus> genericStatuses = genericStatusRepository.findAll();
    assertThat(genericStatuses).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkDescriptionIsRequired() throws Exception {
    int databaseSizeBeforeTest = genericStatusRepository.findAll().size();
    // set the field null
    genericStatus.setDescription(null);

    // Create the GenericStatus, which fails.
    GenericStatusDTO genericStatusDTO = genericStatusMapper.genericStatusToGenericStatusDTO(genericStatus);

    restGenericStatusMockMvc.perform(post("/api/generic-statuses")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(genericStatusDTO)))
      .andExpect(status().isBadRequest());

    List<GenericStatus> genericStatuses = genericStatusRepository.findAll();
    assertThat(genericStatuses).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllGenericStatuses() throws Exception {
    // Initialize the database
    genericStatusRepository.saveAndFlush(genericStatus);

    // Get all the generic-statuses
    restGenericStatusMockMvc.perform(get("/api/generic-statuses"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.[*].id").value(hasItem(genericStatus.getId())))
      .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
      .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
  }

  @Test
  @Transactional
  public void getGenericStatus() throws Exception {
    // Initialize the database
    genericStatusRepository.saveAndFlush(genericStatus);

    // Get the genericStatus
    restGenericStatusMockMvc.perform(get("/api/generic-statuses/{id}", genericStatus.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.id").value(genericStatus.getId()))
      .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
      .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingGenericStatus() throws Exception {
    // Get the genericStatus
    restGenericStatusMockMvc.perform(get("/api/generic-statuses/{id}", Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }
}
