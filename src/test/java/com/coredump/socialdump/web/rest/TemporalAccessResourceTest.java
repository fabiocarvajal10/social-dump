package com.coredump.socialdump.web.rest;

import com.coredump.socialdump.Application;
import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.MonitorContact;
import com.coredump.socialdump.domain.TemporalAccess;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.repository.TemporalAccessRepository;
import com.coredump.socialdump.service.GenericStatusService;
import com.coredump.socialdump.web.rest.dto.TemporalAccessDTO;
import com.coredump.socialdump.web.rest.mapper.TemporalAccessMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TemporalAccessResource REST controller.
 *
 * @see TemporalAccessResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TemporalAccessResourceTest {

  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

  private static final String DEFAULT_EMAIL = "SAMPLE_TEXT@EXAMPLE.COM";
  private static final String UPDATED_EMAIL = "UPDATED_TEXT";
  private static final String DEFAULT_PASSWORD = "SAMPLE_TEXT";
  private static final String UPDATED_PASSWORD = "UPDATED_TEXT";

  private static final DateTime DEFAULT_CREATED_AT = new DateTime(0L, DateTimeZone.UTC);
  private static final DateTime UPDATED_CREATED_AT = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
  private static final String DEFAULT_CREATED_AT_STR = dateTimeFormatter.print(DEFAULT_CREATED_AT);

  private static final DateTime DEFAULT_START_DATE = new DateTime(0L, DateTimeZone.UTC);
  private static final DateTime UPDATED_START_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
  private static final String DEFAULT_START_DATE_STR = dateTimeFormatter.print(DEFAULT_START_DATE);

  private static final DateTime DEFAULT_END_DATE = new DateTime(1L, DateTimeZone.UTC);
  private static final DateTime UPDATED_END_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(1);
  private static final String DEFAULT_END_DATE_STR = dateTimeFormatter.print(DEFAULT_END_DATE);

  @Inject
  private TemporalAccessRepository temporalAccessRepository;

  @Inject
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Inject
  private PasswordEncoder passwordEncoder;

  @Inject
  private GenericStatusService genericStatusService;

  private MockMvc restTemporalAccessMockMvc;

  private TemporalAccess temporalAccess;

  private TemporalAccessDTO temporalAccessDTO;

  @Inject
  private TemporalAccessMapper temporalAccessMapper;

  @Inject
  private EventRepository eventRepository;

  @PostConstruct
  public void setup() {
    MockitoAnnotations.initMocks(this);
    TemporalAccessResource temporalAccessResource = new TemporalAccessResource();
    ReflectionTestUtils.setField(temporalAccessResource,
      "temporalAccessRepository", temporalAccessRepository);
    ReflectionTestUtils.setField(temporalAccessResource,
      "temporalAccessMapper", temporalAccessMapper);
    ReflectionTestUtils.setField(temporalAccessResource,
      "eventRepository", eventRepository);
    ReflectionTestUtils.setField(temporalAccessResource,
      "passwordEncoder", passwordEncoder);
    ReflectionTestUtils.setField(temporalAccessResource,
      "genericStatusService", genericStatusService);
    this.restTemporalAccessMockMvc = MockMvcBuilders
      .standaloneSetup(temporalAccessResource)
      .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    temporalAccess = new TemporalAccess();
    temporalAccess.setEmail(DEFAULT_EMAIL);
    temporalAccess.setPassword(DEFAULT_PASSWORD);
    temporalAccess.setCreatedAt(DEFAULT_CREATED_AT);
    temporalAccess.setStartDate(DEFAULT_START_DATE);
    temporalAccess.setEndDate(DEFAULT_END_DATE);

    temporalAccessDTO = new TemporalAccessDTO();
    temporalAccessDTO.setEmail(DEFAULT_EMAIL);
    temporalAccessDTO.setPassword(DEFAULT_PASSWORD);
    temporalAccessDTO.setCreatedAt(DEFAULT_CREATED_AT);
    temporalAccessDTO.setStartDate(DEFAULT_START_DATE);
    temporalAccessDTO.setEndDate(DEFAULT_END_DATE);
    temporalAccessDTO.setEventId(1L);
    temporalAccessDTO.setMonitorContactId(1L);

    Event e = new Event();
    e.setId(1L);
    temporalAccess.setEventByEventId(e);
    MonitorContact mc = new MonitorContact();
    mc.setId(1L);
    mc.setEmail(DEFAULT_EMAIL);
    temporalAccess.setMonitorContactByMonitorContactId(mc);
  }

  // @Test
  // @Transactional
  // public void createTemporalAccess() throws Exception {
  //   int databaseSizeBeforeCreate = temporalAccessRepository.findAll().size();

  //   // Create the TemporalAccess

  //   restTemporalAccessMockMvc.perform(post("/api/temporal-accesses")
  //     .contentType(TestUtil.APPLICATION_JSON_UTF8)
  //     .content(TestUtil.convertObjectToJsonBytes(temporalAccessDTO)))
  //     .andExpect(status().isCreated());

  //   // Validate the TemporalAccess in the database
  //   List<TemporalAccess> temporalAccesses = temporalAccessRepository.findAll();
  //   assertThat(temporalAccesses).hasSize(databaseSizeBeforeCreate + 1);
  //   TemporalAccess testTemporalAccess = temporalAccesses.get(temporalAccesses.size() - 1);
  //   assertThat(testTemporalAccess.getEmail()).isEqualTo(DEFAULT_EMAIL);
  //   assertThat(testTemporalAccess.getPassword()).isEqualTo(DEFAULT_PASSWORD);
  //   assertThat(testTemporalAccess.getCreatedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATED_AT);
  //   assertThat(testTemporalAccess.getStartDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_START_DATE);
  //   assertThat(testTemporalAccess.getEndDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_END_DATE);
  // }

  // @Test
  // @Transactional
  // public void getAllTemporalAccesses() throws Exception {
  //   // Initialize the database
  //   temporalAccessRepository.saveAndFlush(temporalAccess);

  //   // Get all the temporalAccesses
  //   restTemporalAccessMockMvc.perform(get("/api/temporal-accesses?eventId=1"))
  //     .andExpect(status().isOk())
  //     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
  //     .andExpect(jsonPath("$.[*].id").value(hasItem(temporalAccess.getId().intValue())))
  //     .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
  //     .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
  //     .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT_STR)))
  //     .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
  //     .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)));
  // }

  // @Test
  // @Transactional
  // public void getTemporalAccess() throws Exception {
  //   // Initialize the database
  //   temporalAccessRepository.saveAndFlush(temporalAccess);

  //   // Get the temporalAccess
  //   restTemporalAccessMockMvc.perform(get("/api/temporal-accesses/{id}", temporalAccess.getId()))
  //     .andExpect(status().isOk())
  //     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
  //     .andExpect(jsonPath("$.id").value(temporalAccess.getId()))
  //     .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
  //     .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
  //     .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT_STR))
  //     .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE_STR))
  //     .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR));
  // }

  @Test
  @Transactional
  public void getNonExistingTemporalAccess() throws Exception {
    // Get the temporalAccess
    restTemporalAccessMockMvc.perform(get("/api/temporal-accesses/{id}",
      Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  // @Test
  // @Transactional
  // public void deleteTemporalAccess() throws Exception {
  //   // Initialize the database
  //   temporalAccessRepository.saveAndFlush(temporalAccess);

  //   int databaseSizeBeforeDelete = temporalAccessRepository.findAll().size();

  //   // Get the temporalAccess
  //   restTemporalAccessMockMvc.perform(delete("/api/temporal-accesses/{id}",
  //     temporalAccess.getId())
  //     .accept(TestUtil.APPLICATION_JSON_UTF8))
  //     .andExpect(status().isOk());

  //   // Validate the database is empty
  //   List<TemporalAccess> temporalAccesses = temporalAccessRepository.findAll();
  //   assertThat(temporalAccesses).hasSize(databaseSizeBeforeDelete - 1);
  // }
}
