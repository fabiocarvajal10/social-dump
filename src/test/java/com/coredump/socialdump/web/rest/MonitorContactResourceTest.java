package com.coredump.socialdump.web.rest;

import com.coredump.socialdump.Application;
import com.coredump.socialdump.domain.MonitorContact;
import com.coredump.socialdump.domain.Organization;
import com.coredump.socialdump.repository.MonitorContactRepository;
import com.coredump.socialdump.web.rest.mapper.MonitorContactMapper;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MonitorContactResource REST controller.
 *
 * @see MonitorContactResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MonitorContactResourceTest {

  private static final String DEFAULT_FIRST_NAME = "SAMPLE_TEXT";
  private static final String UPDATED_FIRST_NAME = "UPDATED_TEXT";
  private static final String DEFAULT_LAST_NAME = "SAMPLE_TEXT";
  private static final String UPDATED_LAST_NAME = "UPDATED_TEXT";
  private static final String DEFAULT_EMAIL = "SAMPLE_TEXT";
  private static final String UPDATED_EMAIL = "UPDATED_TEXT";
  private static final String DEFAULT_PHONE = "SAMPLE_TEXT";
  private static final String UPDATED_PHONE = "UPDATED_TEXT";

  @Inject
  private MonitorContactRepository monitorContactRepository;

  @Inject
  private MonitorContactMapper monitorContactMapper;

  @Inject
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  private MockMvc restMonitorContactMockMvc;

  private MonitorContact monitorContact;

  @PostConstruct
  public void setup() {
    MockitoAnnotations.initMocks(this);
    MonitorContactResource monitorContactResource = new MonitorContactResource();
    ReflectionTestUtils.setField(monitorContactResource, "monitorContactRepository", monitorContactRepository);
    ReflectionTestUtils.setField(monitorContactResource, "monitorContactMapper", monitorContactMapper);
    this.restMonitorContactMockMvc =
      MockMvcBuilders.standaloneSetup(monitorContactResource)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    monitorContact = new MonitorContact();
    Organization org = new Organization();
    org.setId(1L);
    monitorContact.setOrganizationByOrganizationId(org);
    monitorContact.setFirstName(DEFAULT_FIRST_NAME);
    monitorContact.setLastName(DEFAULT_LAST_NAME);
    monitorContact.setEmail(DEFAULT_EMAIL);
    monitorContact.setPhone(DEFAULT_PHONE);
  }

  @Test
  @Transactional
  public void createMonitorContact() throws Exception {
    int databaseSizeBeforeCreate = monitorContactRepository.findAll().size();

    // Create the MonitorContact

    restMonitorContactMockMvc.perform(post("/api/monitor-contacts")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(monitorContact)))
      .andExpect(status().isCreated());

    // Validate the MonitorContact in the database
    List<MonitorContact> monitorContacts = monitorContactRepository.findAll();
    assertThat(monitorContacts).hasSize(databaseSizeBeforeCreate + 1);
    MonitorContact testMonitorContact = monitorContacts.get(monitorContacts.size() - 1);
    assertThat(testMonitorContact.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
    assertThat(testMonitorContact.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    assertThat(testMonitorContact.getEmail()).isEqualTo(DEFAULT_EMAIL);
    assertThat(testMonitorContact.getPhone()).isEqualTo(DEFAULT_PHONE);
  }

  @Test
  @Transactional
  public void checkFirstNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = monitorContactRepository.findAll().size();
    // set the field null
    monitorContact.setFirstName(null);

    // Create the MonitorContact, which fails.

    restMonitorContactMockMvc.perform(post("/api/monitor-contacts")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(monitorContact)))
      .andExpect(status().isBadRequest());

    List<MonitorContact> monitorContacts = monitorContactRepository.findAll();
    assertThat(monitorContacts).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLastNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = monitorContactRepository.findAll().size();
    // set the field null
    monitorContact.setLastName(null);

    // Create the MonitorContact, which fails.

    restMonitorContactMockMvc.perform(post("/api/monitor-contacts")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(monitorContact)))
      .andExpect(status().isBadRequest());

    List<MonitorContact> monitorContacts = monitorContactRepository.findAll();
    assertThat(monitorContacts).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllMonitorContacts() throws Exception {
    // Initialize the database
    monitorContactRepository.saveAndFlush(monitorContact);

    // Get all the monitorContacts
    restMonitorContactMockMvc.perform(get("/api/monitor-contacts"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.[*].id").value(hasItem(monitorContact.getId())))
      .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
      .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
      .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
      .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())));
  }

  @Test
  @Transactional
  public void getMonitorContact() throws Exception {
    // Initialize the database
    monitorContactRepository.saveAndFlush(monitorContact);

    // Get the monitorContact
    restMonitorContactMockMvc.perform(get("/api/monitor-contacts/{id}", monitorContact.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.id").value(monitorContact.getId()))
      .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
      .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
      .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
      .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingMonitorContact() throws Exception {
    // Get the monitorContact
    restMonitorContactMockMvc.perform(get("/api/monitor-contacts/{id}", Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateMonitorContact() throws Exception {
    // Initialize the database
    monitorContactRepository.saveAndFlush(monitorContact);

    int databaseSizeBeforeUpdate = monitorContactRepository.findAll().size();

    // Update the monitorContact
    monitorContact.setFirstName(UPDATED_FIRST_NAME);
    monitorContact.setLastName(UPDATED_LAST_NAME);
    monitorContact.setEmail(UPDATED_EMAIL);
    monitorContact.setPhone(UPDATED_PHONE);


    restMonitorContactMockMvc.perform(put("/api/monitor-contacts")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(monitorContact)))
      .andExpect(status().isOk());

    // Validate the MonitorContact in the database
    List<MonitorContact> monitorContacts = monitorContactRepository.findAll();
    assertThat(monitorContacts).hasSize(databaseSizeBeforeUpdate);
    MonitorContact testMonitorContact = monitorContacts.get(monitorContacts.size() - 1);
    assertThat(testMonitorContact.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
    assertThat(testMonitorContact.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    assertThat(testMonitorContact.getEmail()).isEqualTo(UPDATED_EMAIL);
    assertThat(testMonitorContact.getPhone()).isEqualTo(UPDATED_PHONE);
  }

  @Test
  @Transactional
  public void deleteMonitorContact() throws Exception {
    // Initialize the database
    monitorContactRepository.saveAndFlush(monitorContact);

    int databaseSizeBeforeDelete = monitorContactRepository.findAll().size();

    // Get the monitorContact
    restMonitorContactMockMvc.perform(delete("/api/monitor-contacts/{id}", monitorContact.getId())
      .accept(TestUtil.APPLICATION_JSON_UTF8))
      .andExpect(status().isOk());

    // Validate the database is empty
    List<MonitorContact> monitorContacts = monitorContactRepository.findAll();
    assertThat(monitorContacts).hasSize(databaseSizeBeforeDelete - 1);
  }
}
