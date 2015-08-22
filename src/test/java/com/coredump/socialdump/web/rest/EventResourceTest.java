package com.coredump.socialdump.web.rest;

import com.coredump.socialdump.Application;
import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.EventStatus;
import com.coredump.socialdump.domain.EventType;
import com.coredump.socialdump.domain.Organization;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.repository.EventStatusRepository;
import com.coredump.socialdump.repository.OrganizationRepository;
import com.coredump.socialdump.repository.UserRepository;
import com.coredump.socialdump.service.EventService;
import com.coredump.socialdump.service.EventStatusService;
import com.coredump.socialdump.service.OrganizationService;
import com.coredump.socialdump.service.SearchCriteriaService;
import com.coredump.socialdump.service.TemporalAccessService;
import com.coredump.socialdump.web.rest.dto.EventDTO;
import com.coredump.socialdump.web.rest.mapper.EventMapper;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EventResource REST controller.
 *
 * @see EventResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EventResourceTest {


  private static final DateTime DEFAULT_START_DATE = new DateTime(0L);
  private static final DateTime UPDATED_START_DATE = new DateTime()
    .withMillisOfSecond(0);

  private static final DateTime DEFAULT_END_DATE = new DateTime(0L);
  private static final DateTime UPDATED_END_DATE = new DateTime()
    .withMillisOfSecond(0);
  private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
  private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

  private static final DateTime DEFAULT_ACTIVATED_AT = new DateTime(0L);
  private static final DateTime UPDATED_ACTIVATED_AT = new DateTime()
    .withMillisOfSecond(0);

  private static final Integer DEFAULT_POST_DELAY = 0;
  private static final Integer UPDATED_POST_DELAY = 1;

  Organization organization;
  @Inject
  private EventRepository eventRepository;
  @Inject
  private EventMapper eventMapper;
  private MockMvc restEventMockMvc;
  private EventDTO eventDTO;
  private Event event;
  @Inject
  private UserRepository userRepository;
  @Mock
  private EventStatusService mockEventStatusService;
  @Inject
  private OrganizationRepository organizationRepository;
  @Inject
  private SearchCriteriaService searchCriteriaService;
  @Mock
  private EventService mockEventService;
  @Mock
  private OrganizationService mockOrganizationService;
  @Inject
  private EventStatusRepository statusRepository;
  @Inject
  private TemporalAccessService temporalAccessService;

  @PostConstruct
  public void setup() {
    MockitoAnnotations.initMocks(this);
    EventResource eventResource = new EventResource();
    ReflectionTestUtils.setField(eventResource, "eventRepository",
      eventRepository);
    ReflectionTestUtils.setField(eventResource, "eventMapper",
      eventMapper);
    ReflectionTestUtils.setField(eventResource, "searchCriteriaService",
      searchCriteriaService);
    ReflectionTestUtils.setField(eventResource, "eventService",
      mockEventService);

    doReturn(organization).when(mockOrganizationService)
      .ownsOrganization(anyObject());
    ReflectionTestUtils.setField(eventResource, "organizationService",
      mockOrganizationService);

    EventStatus es = new EventStatus();
    es.setId((short) 1);
    es.setStatus("Activo");
    es.setDescription("Estado Activo");

    doReturn(es).when(mockEventStatusService).getActive();
    ReflectionTestUtils.setField(eventResource, "eventStatusService",
      mockEventStatusService);

    organization = organizationRepository.findAll().get(0);

    ReflectionTestUtils.setField(eventResource, "statusRepository",
      statusRepository);

    ReflectionTestUtils.setField(eventResource, "temporalAccessService",
      temporalAccessService);

    this.restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource)
      .build();
  }

  @Before
  public void initTest() {
    eventDTO = new EventDTO();
    eventDTO.setStartDate(DEFAULT_START_DATE);
    eventDTO.setEndDate(DEFAULT_END_DATE);
    eventDTO.setDescription(DEFAULT_DESCRIPTION);
    eventDTO.setActivatedAt(DEFAULT_ACTIVATED_AT);
    eventDTO.setPostDelay(DEFAULT_POST_DELAY);
    TestUtil.login(organization.getUserByOwnerId().getLogin(),
      organization.getUserByOwnerId().getPassword());
    eventDTO.setOrganizationId(organization.getId());
    EventStatus es = new EventStatus();
    es.setId((short) 1);
    eventDTO.setStatusId(es.getId());
    EventType et = new EventType();
    et.setId(1);
    eventDTO.setTypeId(et.getId());
    eventDTO.setSearchCriterias(Arrays.asList("1", "Uno"));

    event = new Event();
    event.setStartDate(DEFAULT_START_DATE);
    event.setEndDate(DEFAULT_END_DATE);
    event.setDescription(DEFAULT_DESCRIPTION);
    event.setActivatedAt(DEFAULT_ACTIVATED_AT);
    event.setPostDelay(DEFAULT_POST_DELAY);
    event.setOrganizationByOrganizationId(organization);
    event.setEventStatusByStatusId(es);
    event.setEventTypeByEventTypeId(et);
  }

  @Test
  @Transactional
  public void createEvent() throws Exception {
    int databaseSizeBeforeCreate = eventRepository.findAll().size();

    // Create the Event
    restEventMockMvc.perform(post("/api/events")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
      .andExpect(status().isCreated());

    // Validate the Event in the database
    List<Event> events = eventRepository.findAll();
    assertThat(events).hasSize(databaseSizeBeforeCreate + 1);
    Event testEvent = events.get(events.size() - 1);
    assertThat(testEvent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
    assertThat(testEvent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    assertThat(testEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testEvent.getActivatedAt()).isEqualTo(DEFAULT_ACTIVATED_AT);
    assertThat(testEvent.getPostDelay()).isEqualTo(DEFAULT_POST_DELAY);
  }

  @Test
  @Transactional
  public void checkStartDateIsRequired() throws Exception {
    int databaseSizeBeforeTest = eventRepository.findAll().size();
    // set the field null
    eventDTO.setStartDate(null);

    // Create the Event, which fails.
    restEventMockMvc.perform(post("/api/events")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
      .andExpect(status().isBadRequest());

    List<Event> events = eventRepository.findAll();
    assertThat(events).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkEndDateIsRequired() throws Exception {
    int databaseSizeBeforeTest = eventRepository.findAll().size();
    // set the field null
    eventDTO.setEndDate(null);

    // Create the Event, which fails.
    restEventMockMvc.perform(post("/api/events")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
      .andExpect(status().isBadRequest());

    List<Event> events = eventRepository.findAll();
    assertThat(events).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkDescriptionIsRequired() throws Exception {
    int databaseSizeBeforeTest = eventRepository.findAll().size();
    // set the field null
    eventDTO.setDescription(null);

    // Create the Event, which fails.
    restEventMockMvc.perform(post("/api/events")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
      .andExpect(status().isBadRequest());

    List<Event> events = eventRepository.findAll();
    assertThat(events).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkPostDelayIsRequired() throws Exception {
    int databaseSizeBeforeTest = eventRepository.findAll().size();
    // set the field null
    eventDTO.setPostDelay(null);

    // Create the Event, which fails.
    restEventMockMvc.perform(post("/api/events")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
      .andExpect(status().isBadRequest());

    List<Event> events = eventRepository.findAll();
    assertThat(events).hasSize(databaseSizeBeforeTest);
  }

  // @Test
  // @Transactional
  // public void getAllEvents() throws Exception {
  //   // Initialize the database
  //   eventRepository.saveAndFlush(event);

  //   // Get all the events
  //   restEventMockMvc.perform(get("/api/events?organizationId={}",
  //       organization.getId()))
  //     .andExpect(status().isOk())
  //     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
  //     .andExpect(jsonPath("$.[*].id")
  //       .value(hasItem(eventDTO.getId().intValue())))
  //     .andExpect(jsonPath("$.[*].startDate")
  //       .value(hasItem(DEFAULT_START_DATE.toString())))
  //     .andExpect(jsonPath("$.[*].endDate")
  //       .value(hasItem(DEFAULT_END_DATE.toString())))
  //     .andExpect(jsonPath("$.[*].description")
  //       .value(hasItem(DEFAULT_DESCRIPTION.toString())))
  //     .andExpect(jsonPath("$.[*].activatedAt")
  //       .value(hasItem(DEFAULT_ACTIVATED_AT.toString())))
  //     .andExpect(jsonPath("$.[*].postDelay")
  //       .value(hasItem(DEFAULT_POST_DELAY)));
  // }

  // @Test
  // @Transactional
  // public void getEvent() throws Exception {
  //   // Initialize the database
  //   eventRepository.saveAndFlush(event);

  //   doReturn(organization).when(mockOrganizationService)
  //     .ownsOrganization(anyObject());

  //   // Get the event
  //   restEventMockMvc.perform(get("/api/events/{id}", event.getId()))
  //     .andExpect(status().isOk())
  //     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
  //     .andExpect(jsonPath("$.id").value(event.getId().intValue()))
  //     .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
  //     .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
  //     .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
  //     .andExpect(jsonPath("$.activatedAt").value(DEFAULT_ACTIVATED_AT.toString()))
  //     .andExpect(jsonPath("$.postDelay").value(DEFAULT_POST_DELAY));
  // }

  @Test
  @Transactional
  public void getNonExistingEvent() throws Exception {
    // Get the event
    restEventMockMvc.perform(get("/api/events/{id}", Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  // @Test
  // @Transactional
  // public void updateEvent() throws Exception {
  //   // Initialize the database
  //   event = eventRepository.saveAndFlush(event);

  //   int databaseSizeBeforeUpdate = eventRepository.findAll().size();

  //   // Update the event
  //   eventDTO.setId(event.getId());
  //   eventDTO.setStartDate(UPDATED_START_DATE);
  //   eventDTO.setEndDate(UPDATED_END_DATE);
  //   eventDTO.setDescription(UPDATED_DESCRIPTION);
  //   eventDTO.setActivatedAt(UPDATED_ACTIVATED_AT);
  //   eventDTO.setPostDelay(UPDATED_POST_DELAY);

  //   doReturn(organization).when(mockOrganizationService)
  //     .ownsOrganization(anyObject());

  //   restEventMockMvc.perform(put("/api/events")
  //     .contentType(TestUtil.APPLICATION_JSON_UTF8)
  //     .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
  //     .andExpect(status().isOk());

  //   // Validate the Event in the database
  //   List<Event> events = eventRepository.findAll();
  //   assertThat(events).hasSize(databaseSizeBeforeUpdate);
  //   Event testEvent = events.get(events.size() - 1);
  //   assertThat(testEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
  //   assertThat(testEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
  //   assertThat(testEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  //   assertThat(testEvent.getActivatedAt()).isEqualTo(UPDATED_ACTIVATED_AT);
  //   assertThat(testEvent.getPostDelay()).isEqualTo(UPDATED_POST_DELAY);
  // }

  @Test
  @Transactional
  public void deleteEvent() throws Exception {
    // Initialize the database
    eventRepository.saveAndFlush(event);

    int databaseSizeBeforeDelete = eventRepository.findAll().size();

    TestUtil.login(organization.getUserByOwnerId().getLogin(), "password");


    doReturn(organization).when(mockOrganizationService)
      .ownsOrganization(anyObject());

    // Get the event
    restEventMockMvc.perform(delete("/api/events/{id}", event.getId())
      .accept(TestUtil.APPLICATION_JSON_UTF8))
      .andExpect(status().isOk());

    event = eventRepository.findOne(event.getId());

    // Validate the database is empty
    List<Event> events = eventRepository.findAll();
    assertThat(event.getEventStatusByStatusId().getStatus())
      .isEqualToIgnoringCase("Cancelado");
  }
}
