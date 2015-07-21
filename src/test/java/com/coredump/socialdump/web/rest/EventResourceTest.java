package com.coredump.socialdump.web.rest;

import com.coredump.socialdump.Application;
import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.web.rest.mapper.EventMapper;

import org.joda.time.DateTime;
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
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    private static final DateTime UPDATED_START_DATE = new DateTime();

    private static final DateTime DEFAULT_END_DATE = new DateTime(0L);
    private static final DateTime UPDATED_END_DATE = new DateTime();
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final DateTime DEFAULT_ACTIVATED_AT = new DateTime(0L);
    private static final DateTime UPDATED_ACTIVATED_AT = new DateTime();

    private static final Integer DEFAULT_POST_DELAY = 0;
    private static final Integer UPDATED_POST_DELAY = 1;

    @Inject
    private EventRepository eventRepository;

    @Inject
    private EventMapper eventMapper;

    private MockMvc restEventMockMvc;

    private Event event;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EventResource eventResource = new EventResource();
        ReflectionTestUtils.setField(eventResource, "eventRepository", eventRepository);
        ReflectionTestUtils.setField(eventResource, "eventMapper", eventMapper);
        this.restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource).build();
    }

    @Before
    public void initTest() {
        event = new Event();
        event.setStartDate(DEFAULT_START_DATE);
        event.setEndDate(DEFAULT_END_DATE);
        event.setDescription(DEFAULT_DESCRIPTION);
        event.setActivatedAt(DEFAULT_ACTIVATED_AT);
        event.setPostDelay(DEFAULT_POST_DELAY);
    }

    @Test
    @Transactional
    public void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event
        restEventMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event)))
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
        // Validate the database is empty
        assertThat(eventRepository.findAll()).hasSize(0);
        // set the field null
        event.setStartDate(null);

        // Create the Event, which fails.
        restEventMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(0);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(eventRepository.findAll()).hasSize(0);
        // set the field null
        event.setEndDate(null);

        // Create the Event, which fails.
        restEventMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(0);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(eventRepository.findAll()).hasSize(0);
        // set the field null
        event.setDescription(null);

        // Create the Event, which fails.
        restEventMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(0);
    }

    @Test
    @Transactional
    public void checkPostDelayIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(eventRepository.findAll()).hasSize(0);
        // set the field null
        event.setPostDelay(1);

        // Create the Event, which fails.
        restEventMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the events
        restEventMockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].activatedAt").value(hasItem(DEFAULT_ACTIVATED_AT.toString())))
                .andExpect(jsonPath("$.[*].postDelay").value(hasItem(DEFAULT_POST_DELAY)));
    }

    @Test
    @Transactional
    public void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.activatedAt").value(DEFAULT_ACTIVATED_AT.toString()))
            .andExpect(jsonPath("$.postDelay").value(DEFAULT_POST_DELAY));
    }

    @Test
    @Transactional
    public void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

		int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        event.setStartDate(UPDATED_START_DATE);
        event.setEndDate(UPDATED_END_DATE);
        event.setDescription(UPDATED_DESCRIPTION);
        event.setActivatedAt(UPDATED_ACTIVATED_AT);
        event.setPostDelay(UPDATED_POST_DELAY);
        restEventMockMvc.perform(put("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event)))
                .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = events.get(events.size() - 1);
        assertThat(testEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEvent.getActivatedAt()).isEqualTo(UPDATED_ACTIVATED_AT);
        assertThat(testEvent.getPostDelay()).isEqualTo(UPDATED_POST_DELAY);
    }

    @Test
    @Transactional
    public void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

		int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Get the event
        restEventMockMvc.perform(delete("/api/events/{id}", event.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeDelete - 1);
    }
}
