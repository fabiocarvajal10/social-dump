package com.coredump.socialdump.web.rest;

import com.coredump.socialdump.Application;
import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.repository.SearchCriteriaRepository;
import com.coredump.socialdump.web.rest.mapper.SearchCriteriaMapper;

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
 * Test class for the SearchCriteriaResource REST controller.
 *
 * @see SearchCriteriaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SearchCriteriaResourceTest {

    private static final String DEFAULT_SEARCH_CRITERIA = "SAMPLE_TEXT";
    private static final String UPDATED_SEARCH_CRITERIA = "UPDATED_TEXT";

    @Inject
    private SearchCriteriaRepository searchCriteriaRepository;

    @Inject
    private SearchCriteriaMapper searchCriteriaMapper;

    private MockMvc restSearchCriteriaMockMvc;

    private SearchCriteria searchCriteria;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SearchCriteriaResource searchCriteriaResource = new SearchCriteriaResource();
        ReflectionTestUtils.setField(searchCriteriaResource, "searchCriteriaRepository", searchCriteriaRepository);
        ReflectionTestUtils.setField(searchCriteriaResource, "searchCriteriaMapper", searchCriteriaMapper);
        this.restSearchCriteriaMockMvc = MockMvcBuilders.standaloneSetup(searchCriteriaResource).build();
    }

    @Before
    public void initTest() {
        searchCriteria = new SearchCriteria();
        searchCriteria.setSearchCriteria(DEFAULT_SEARCH_CRITERIA);
    }

    @Test
    @Transactional
    public void createSearchCriteria() throws Exception {
        int databaseSizeBeforeCreate = searchCriteriaRepository.findAll().size();

        // Create the SearchCriteria
        restSearchCriteriaMockMvc.perform(post("/api/searchCriterias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(searchCriteria)))
                .andExpect(status().isCreated());

        // Validate the SearchCriteria in the database
        List<SearchCriteria> searchCriterias = searchCriteriaRepository.findAll();
        assertThat(searchCriterias).hasSize(databaseSizeBeforeCreate + 1);
        SearchCriteria testSearchCriteria = searchCriterias.get(searchCriterias.size() - 1);
        assertThat(testSearchCriteria.getSearchCriteria()).isEqualTo(DEFAULT_SEARCH_CRITERIA);
    }

    @Test
    @Transactional
    public void checkSearchCriteriaIsRequired() throws Exception {
        int databaseSizeBeforeTest = searchCriteriaRepository.findAll().size();
        // set the field null
        searchCriteria.setSearchCriteria(null);

        // Create the SearchCriteria, which fails.
        restSearchCriteriaMockMvc.perform(post("/api/searchCriterias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(searchCriteria)))
                .andExpect(status().isBadRequest());

        List<SearchCriteria> searchCriterias = searchCriteriaRepository.findAll();
        assertThat(searchCriterias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSearchCriterias() throws Exception {
        // Initialize the database
        searchCriteriaRepository.saveAndFlush(searchCriteria);

        // Get all the searchCriterias
        restSearchCriteriaMockMvc.perform(get("/api/searchCriterias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(searchCriteria.getId().intValue())))
                .andExpect(jsonPath("$.[*].searchCriteria").value(hasItem(DEFAULT_SEARCH_CRITERIA.toString())));
    }

    @Test
    @Transactional
    public void getSearchCriteria() throws Exception {
        // Initialize the database
        searchCriteriaRepository.saveAndFlush(searchCriteria);

        // Get the searchCriteria
        restSearchCriteriaMockMvc.perform(get("/api/searchCriterias/{id}", searchCriteria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(searchCriteria.getId().intValue()))
            .andExpect(jsonPath("$.searchCriteria").value(DEFAULT_SEARCH_CRITERIA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSearchCriteria() throws Exception {
        // Get the searchCriteria
        restSearchCriteriaMockMvc.perform(get("/api/searchCriterias/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSearchCriteria() throws Exception {
        // Initialize the database
        searchCriteriaRepository.saveAndFlush(searchCriteria);

		int databaseSizeBeforeUpdate = searchCriteriaRepository.findAll().size();

        // Update the searchCriteria
        searchCriteria.setSearchCriteria(UPDATED_SEARCH_CRITERIA);
        restSearchCriteriaMockMvc.perform(put("/api/searchCriterias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(searchCriteria)))
                .andExpect(status().isOk());

        // Validate the SearchCriteria in the database
        List<SearchCriteria> searchCriterias = searchCriteriaRepository.findAll();
        assertThat(searchCriterias).hasSize(databaseSizeBeforeUpdate);
        SearchCriteria testSearchCriteria = searchCriterias.get(searchCriterias.size() - 1);
        assertThat(testSearchCriteria.getSearchCriteria()).isEqualTo(UPDATED_SEARCH_CRITERIA);
    }

    @Test
    @Transactional
    public void deleteSearchCriteria() throws Exception {
        // Initialize the database
        searchCriteriaRepository.saveAndFlush(searchCriteria);

		int databaseSizeBeforeDelete = searchCriteriaRepository.findAll().size();

        // Get the searchCriteria
        restSearchCriteriaMockMvc.perform(delete("/api/searchCriterias/{id}", searchCriteria.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SearchCriteria> searchCriterias = searchCriteriaRepository.findAll();
        assertThat(searchCriterias).hasSize(databaseSizeBeforeDelete - 1);
    }
}
