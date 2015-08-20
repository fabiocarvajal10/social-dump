package com.coredump.socialdump.web.rest;

import com.coredump.socialdump.Application;
import com.coredump.socialdump.domain.Organization;
import com.coredump.socialdump.domain.User;
import com.coredump.socialdump.repository.OrganizationRepository;
import com.coredump.socialdump.service.OrganizationService;
import com.coredump.socialdump.web.rest.dto.OrganizationDTO;
import com.coredump.socialdump.web.rest.mapper.OrganizationMapper;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrganizationResource REST controller.
 *
 * @see OrganizationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OrganizationResourceTest {

  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

  private static final String DEFAULT_NAME = UUID.randomUUID().toString();
  private static final String UPDATED_NAME = UUID.randomUUID().toString();

  private static final DateTime DEFAULT_CREATED_AT = new DateTime(0L, DateTimeZone.UTC);
  private static final String DEFAULT_CREATED_AT_STR = dateTimeFormatter.print(DEFAULT_CREATED_AT);


  private static final DateTime UPDATED_CREATED_AT = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);

  @Inject
  private OrganizationRepository organizationRepository;

  @Inject
  private OrganizationService organizationService;

  @Inject
  private OrganizationMapper organizationMapper;

  @Inject
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  private MockMvc restOrganizationMockMvc;

  private Organization organization;

  private OrganizationDTO organizationDTO;

  @Inject
  private TestUtil testUtil;

  @PostConstruct
  public void setup() {
    MockitoAnnotations.initMocks(this);
    OrganizationResource organizationResource = new OrganizationResource();
    ReflectionTestUtils.setField(organizationResource, "organizationRepository",
      organizationRepository);
    ReflectionTestUtils.setField(organizationResource, "organizationService",
      organizationService);
    ReflectionTestUtils.setField(organizationResource, "organizationMapper",
      organizationMapper);
    this.restOrganizationMockMvc =
      MockMvcBuilders.standaloneSetup(organizationResource)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    organization = new Organization();
    organization.setName(DEFAULT_NAME);
    User u = new User();
    u.setId(1L);
    organization.setUserByOwnerId(u);
    organization.setCreatedAt(DEFAULT_CREATED_AT);

    organizationDTO = new OrganizationDTO();
    organizationDTO.setOwnerId(3L);
    organizationDTO.setName(DEFAULT_NAME);
  }

  @Test
  @Transactional
  public void createOrganization() throws Exception {
    int databaseSizeBeforeCreate = organizationRepository.findAll().size();

    testUtil.loginFromDB("admin", "admin");
    // Create the Organization

    restOrganizationMockMvc.perform(post("/api/organizations")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(organizationDTO)))
      .andExpect(status().isCreated());

    // Validate the Organization in the database
    List<Organization> organizations = organizationRepository.findAll();
    assertThat(organizations).hasSize(databaseSizeBeforeCreate + 1);
    Organization testOrganization = organizations.get(organizations.size() - 1);
    assertThat(testOrganization.getName()).isEqualTo(DEFAULT_NAME);
  }

  @Test
  @Transactional
  public void checkNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = organizationRepository.findAll().size();
    // set the field null
    organization.setName(null);

    // Create the Organization, which fails.
    testUtil.loginFromDB("admin", "admin");

    restOrganizationMockMvc.perform(post("/api/organizations")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(organization)))
      .andExpect(status().isBadRequest());

    List<Organization> organizations = organizationRepository.findAll();
    assertThat(organizations).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllOrganizations() throws Exception {
    testUtil.loginFromDB("admin", "admin");

    // Initialize the database
    organizationRepository.saveAndFlush(organization);

    // Get all the organizations
    restOrganizationMockMvc.perform(get("/api/organizations"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().intValue())))
      .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
  }

  @Test
  @Transactional
  public void getOrganization() throws Exception {
    testUtil.loginFromDB("admin", "admin");

    // Initialize the database
    organizationRepository.saveAndFlush(organization);

    // Get the organization
    restOrganizationMockMvc.perform(get("/api/organizations/{id}", organization.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.id").value(organization.getId().intValue()))
      .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingOrganization() throws Exception {
    testUtil.loginFromDB("admin", "admin");
    // Get the organization
    restOrganizationMockMvc.perform(get("/api/organizations/{id}", Long.MAX_VALUE))
      .andExpect(status().isForbidden());
  }

  @Test
  @Transactional
  public void updateOrganization() throws Exception {
    // Initialize the database
    organizationRepository.saveAndFlush(organization);

    int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

    testUtil.loginFromDB("admin", "admin");

    // Update the organization
    organization.setName(UPDATED_NAME);
    organization.setCreatedAt(UPDATED_CREATED_AT);


    restOrganizationMockMvc.perform(put("/api/organizations")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(organization)))
      .andExpect(status().isOk());

    // Validate the Organization in the database
    List<Organization> organizations = organizationRepository.findAll();
    assertThat(organizations).hasSize(databaseSizeBeforeUpdate);
    Organization testOrganization = organizations.get(organizations.size() - 1);
    assertThat(testOrganization.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testOrganization.getCreatedAt().toDateTime(DateTimeZone.UTC))
      .isEqualTo(UPDATED_CREATED_AT);
  }

  @Test
  @Transactional
  public void deleteOrganization() throws Exception {
    // Initialize the database
    testUtil.loginFromDB("admin", "admin");
    organizationRepository.saveAndFlush(organization);

    int databaseSizeBeforeDelete = organizationRepository.findAll().size();


    // Get the organization
    restOrganizationMockMvc.perform(delete("/api/organizations/{id}",
        organization.getId())
      .accept(TestUtil.APPLICATION_JSON_UTF8))
      .andExpect(status().isOk());

    // Validate the database is empty
    List<Organization> organizations = organizationRepository.findAll();
    assertThat(organizations).hasSize(databaseSizeBeforeDelete - 1);
  }
}
