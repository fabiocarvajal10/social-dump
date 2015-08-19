package com.coredump.socialdump.web.rest;

import com.coredump.socialdump.Application;
import com.coredump.socialdump.domain.EventStatus;
import com.coredump.socialdump.repository.EventStatusRepository;
import com.coredump.socialdump.web.rest.mapper.EventStatusMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private static final String DEFAULT_STATUS = "SAMPLE_TEXT";
    private static final String UPDATED_STATUS = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private EventStatusRepository eventStatusRepository;

    @Inject
    private EventStatusMapper eventStatusMapper;

    private MockMvc restEventStatusMockMvc;

    private EventStatus eventStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EventStatusResource eventStatusResource = new EventStatusResource();
        ReflectionTestUtils.setField(eventStatusResource, "eventStatusRepository", eventStatusRepository);
        ReflectionTestUtils.setField(eventStatusResource, "eventStatusMapper", eventStatusMapper);
        this.restEventStatusMockMvc = MockMvcBuilders.standaloneSetup(eventStatusResource).build();
    }

    @Before
    public void initTest() {
        eventStatus = new EventStatus();
        eventStatus.setStatus(DEFAULT_STATUS);
        eventStatus.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createEventStatus() throws Exception {
        int databaseSizeBeforeCreate = eventStatusRepository.findAll().size();

        // Create the EventStatus
        restEventStatusMockMvc.perform(post("/api/event-statuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eventStatus)))
                .andExpect(status().isCreated());

        // Validate the EventStatus in the database
        List<EventStatus> eventStatuses = eventStatusRepository.findAll();
        assertThat(eventStatuses).hasSize(databaseSizeBeforeCreate + 1);
        EventStatus testEventStatus = eventStatuses.get(eventStatuses.size() - 1);
        assertThat(testEventStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEventStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(eventStatusRepository.findAll()).hasSize(0);
        // set the field null
        eventStatus.setStatus(null);

        // Create the EventStatus, which fails.
        restEventStatusMockMvc.perform(post("/api/event-statuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eventStatus)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<EventStatus> eventStatuss = eventStatusRepository.findAll();
        assertThat(eventStatuss).hasSize(0);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(eventStatusRepository.findAll()).hasSize(0);
        // set the field null
        eventStatus.setDescription(null);

        // Create the EventStatus, which fails.
        restEventStatusMockMvc.perform(post("/api/event-statuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eventStatus)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<EventStatus> eventStatuss = eventStatusRepository.findAll();
        assertThat(eventStatuss).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllEventStatuss() throws Exception {
        // Initialize the database
        eventStatusRepository.saveAndFlush(eventStatus);

        // Get all the eventStatuss
        restEventStatusMockMvc.perform(get("/api/event-statuses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(eventStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getEventStatus() throws Exception {
        // Initialize the database
        eventStatusRepository.saveAndFlush(eventStatus);

        // Get the eventStatus
        restEventStatusMockMvc.perform(get("/api/event-statuses/{id}", eventStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(eventStatus.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEventStatus() throws Exception {
        // Get the eventStatus
        restEventStatusMockMvc.perform(get("/api/event-statuses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }
}
