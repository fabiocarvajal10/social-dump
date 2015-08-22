package com.coredump.socialdump.web.rest;

import com.coredump.socialdump.Application;
import com.coredump.socialdump.domain.EventType;
import com.coredump.socialdump.repository.EventTypeRepository;
import com.coredump.socialdump.web.rest.mapper.EventTypeMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
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
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EventTypeResource REST controller.
 *
 * @see EventTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EventTypeResourceTest {

  private static final String DEFAULT_NAME = "Comercial";
  private static final String DEFAULT_DESCRIPTION = "Público, visible abiertamente";

  @Inject
  private EventTypeRepository eventTypeRepository;

  @Inject
  private EventTypeMapper eventTypeMapper;

  private MockMvc restEventTypeMockMvc;

  private EventType eventType;

  @PostConstruct
  public void setup() {
    MockitoAnnotations.initMocks(this);
    EventTypeResource eventTypeResource = new EventTypeResource();
    ReflectionTestUtils.setField(eventTypeResource,
      "eventTypeRepository", eventTypeRepository);
    ReflectionTestUtils.setField(eventTypeResource,
      "eventTypeMapper", eventTypeMapper);
    this.restEventTypeMockMvc = MockMvcBuilders.standaloneSetup(
      eventTypeResource).build();
  }

  @Before
  public void initTest() {
    eventType = new EventType();
    eventType.setName(DEFAULT_NAME);
    eventType.setDescription(DEFAULT_DESCRIPTION);
  }

  // @Test
  // @Transactional
  // public void getAllEventTypes() throws Exception {
  //   // Initialize the database
  //   eventTypeRepository.saveAndFlush(eventType);

  //   // Get all the eventTypes
  //   restEventTypeMockMvc.perform(get("/api/event-types"))
  //     .andExpect(status().isOk())
  //     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
  //     .andExpect(jsonPath("$.[*].id").value(hasItem(eventType.getId().intValue())))
  //     .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
  //     .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
  // }

  // @Test
  // @Transactional
  // public void getEventType() throws Exception {
  //   // Initialize the database
  //   eventTypeRepository.saveAndFlush(eventType);

  //   // Get the eventType
  //   restEventTypeMockMvc.perform(get("/api/event-types/{id}", eventType.getId()))
  //     .andExpect(status().isOk())
  //     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
  //     .andExpect(jsonPath("$.id").value(eventType.getId().intValue()))
  //     .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
  //     .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
  // }

  @Test
  @Transactional
  public void getNonExistingEventType() throws Exception {
    // Get the eventType
    restEventTypeMockMvc.perform(get("/api/event-types/{id}", Short.MAX_VALUE))
      .andExpect(status().isNotFound());
  }
}
