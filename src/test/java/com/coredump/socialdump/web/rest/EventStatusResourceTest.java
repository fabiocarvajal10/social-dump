package com.coredump.socialdump.web.rest;

import com.coredump.socialdump.Application;
import com.coredump.socialdump.repository.EventStatusRepository;
import com.coredump.socialdump.web.rest.mapper.EventStatusMapper;
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

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EventStatusResource REST controller.
 *
 * @see EventStatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EventStatusResourceTest {

  private static final Short DEFAULT_ID = 1;
  private static final String DEFAULT_STATUS = "Activo";
  private static final String DEFAULT_DESCRIPTION = "Estado Activo";

  @Inject
  private EventStatusRepository eventStatusRepository;

  @Inject
  private EventStatusMapper eventStatusMapper;

  private MockMvc restEventStatusMockMvc;

//  private EventStatus eventStatus;

  @PostConstruct
  public void setup() {
    MockitoAnnotations.initMocks(this);
    EventStatusResource eventStatusResource = new EventStatusResource();
    ReflectionTestUtils.setField(eventStatusResource, "eventStatusRepository",
      eventStatusRepository);
    ReflectionTestUtils.setField(eventStatusResource, "eventStatusMapper",
      eventStatusMapper);
    this.restEventStatusMockMvc =
      MockMvcBuilders.standaloneSetup(eventStatusResource).build();
  }

  @Before
  public void initTest() {
  }

  // @Test
  // @Transactional
  // public void getAllEventStatuses() throws Exception {

  //   // Get all the eventStatuses
  //   restEventStatusMockMvc.perform(get("/api/event-statuses"))
  //     .andExpect(status().isOk())
  //     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
  //     .andExpect(jsonPath("$.[*].id").value(hasItem(DEFAULT_ID)))
  //     .andExpect(jsonPath("$.[*].status")
  //       .value(hasItem(DEFAULT_STATUS.toString())))
  //     .andExpect(jsonPath("$.[*].description")
  //       .value(hasItem(DEFAULT_DESCRIPTION.toString())));
  // }

  // @Test
  // @Transactional
  // public void getEventStatus() throws Exception {

  //   // Get the eventStatus
  //   restEventStatusMockMvc.perform(get("/api/event-statuses/{id}", DEFAULT_ID))
  //     .andExpect(status().isOk())
  //     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
  //     .andExpect(jsonPath("$.id").value(DEFAULT_ID))
  //     .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
  //     .andExpect(jsonPath("$.description")
  //       .value(DEFAULT_DESCRIPTION.toString()));
  // }

  @Test
  @Transactional
  public void getNonExistingEventStatus() throws Exception {
    // Get the eventStatus
    restEventStatusMockMvc.perform(get("/api/event-statuses/{id}",
      Short.MAX_VALUE))
      .andExpect(status().isNotFound());
  }
}
