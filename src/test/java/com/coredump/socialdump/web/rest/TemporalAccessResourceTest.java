package com.coredump.socialdump.web.rest;

import com.coredump.socialdump.Application;
import com.coredump.socialdump.domain.TemporalAccess;
import com.coredump.socialdump.repository.TemporalAccessRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    private static final String DEFAULT_EMAIL = "SAMPLE_TEXT";
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

    private MockMvc restTemporalAccessMockMvc;

    private TemporalAccess temporalAccess;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TemporalAccessResource temporalAccessResource = new TemporalAccessResource();
        ReflectionTestUtils.setField(temporalAccessResource, "temporalAccessRepository", temporalAccessRepository);
        this.restTemporalAccessMockMvc = MockMvcBuilders.standaloneSetup(temporalAccessResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        temporalAccess = new TemporalAccess();
        temporalAccess.setEmail(DEFAULT_EMAIL);
        temporalAccess.setPassword(DEFAULT_PASSWORD);
        temporalAccess.setCreatedAt(DEFAULT_CREATED_AT);
        temporalAccess.setStartDate(DEFAULT_START_DATE);
        temporalAccess.setEndDate(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createTemporalAccess() throws Exception {
        int databaseSizeBeforeCreate = temporalAccessRepository.findAll().size();

        // Create the TemporalAccess

        restTemporalAccessMockMvc.perform(post("/api/temporal-accesss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(temporalAccess)))
                .andExpect(status().isCreated());

        // Validate the TemporalAccess in the database
        List<TemporalAccess> temporalAccesss = temporalAccessRepository.findAll();
        assertThat(temporalAccesss).hasSize(databaseSizeBeforeCreate + 1);
        TemporalAccess testTemporalAccess = temporalAccesss.get(temporalAccesss.size() - 1);
        assertThat(testTemporalAccess.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTemporalAccess.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testTemporalAccess.getCreatedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTemporalAccess.getStartDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTemporalAccess.getEndDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTemporalAccesss() throws Exception {
        // Initialize the database
        temporalAccessRepository.saveAndFlush(temporalAccess);

        // Get all the temporalAccesss
        restTemporalAccessMockMvc.perform(get("/api/temporal-accesss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(temporalAccess.getId())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT_STR)))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)));
    }

    @Test
    @Transactional
    public void getTemporalAccess() throws Exception {
        // Initialize the database
        temporalAccessRepository.saveAndFlush(temporalAccess);

        // Get the temporalAccess
        restTemporalAccessMockMvc.perform(get("/api/temporal-accesss/{id}", temporalAccess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(temporalAccess.getId()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT_STR))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingTemporalAccess() throws Exception {
        // Get the temporalAccess
        restTemporalAccessMockMvc.perform(get("/api/temporal-accesss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTemporalAccess() throws Exception {
        // Initialize the database
        temporalAccessRepository.saveAndFlush(temporalAccess);

		int databaseSizeBeforeUpdate = temporalAccessRepository.findAll().size();

        // Update the temporalAccess
        temporalAccess.setEmail(UPDATED_EMAIL);
        temporalAccess.setPassword(UPDATED_PASSWORD);
        temporalAccess.setCreatedAt(UPDATED_CREATED_AT);
        temporalAccess.setStartDate(UPDATED_START_DATE);
        temporalAccess.setEndDate(UPDATED_END_DATE);


        restTemporalAccessMockMvc.perform(put("/api/temporal-accesss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(temporalAccess)))
                .andExpect(status().isOk());

        // Validate the TemporalAccess in the database
        List<TemporalAccess> temporalAccesss = temporalAccessRepository.findAll();
        assertThat(temporalAccesss).hasSize(databaseSizeBeforeUpdate);
        TemporalAccess testTemporalAccess = temporalAccesss.get(temporalAccesss.size() - 1);
        assertThat(testTemporalAccess.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTemporalAccess.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testTemporalAccess.getCreatedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTemporalAccess.getStartDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_START_DATE);
        assertThat(testTemporalAccess.getEndDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void deleteTemporalAccess() throws Exception {
        // Initialize the database
        temporalAccessRepository.saveAndFlush(temporalAccess);

		int databaseSizeBeforeDelete = temporalAccessRepository.findAll().size();

        // Get the temporalAccess
        restTemporalAccessMockMvc.perform(delete("/api/temporal-accesss/{id}", temporalAccess.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TemporalAccess> temporalAccesss = temporalAccessRepository.findAll();
        assertThat(temporalAccesss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
