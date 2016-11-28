package com.aliens.hipster.web.rest;

import com.aliens.hipster.HolmesaggregatorApp;
import com.aliens.hipster.domain.InboundMessages;
import com.aliens.hipster.repository.InboundMessagesRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InboundMessagesResource REST controller.
 *
 * @see InboundMessagesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HolmesaggregatorApp.class)
public class InboundMessagesResourceIntTest {
    private static final String DEFAULT_MESSAGE_ID = "AAAAA";
    private static final String UPDATED_MESSAGE_ID = "BBBBB";
    private static final String DEFAULT_GROUP_ID = "AAAAA";
    private static final String UPDATED_GROUP_ID = "BBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_PAYLOAD = "AAAAA";
    private static final String UPDATED_PAYLOAD = "BBBBB";

    @Inject
    private InboundMessagesRepository inboundMessagesRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restInboundMessagesMockMvc;

    private InboundMessages inboundMessages;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InboundMessagesResource inboundMessagesResource = new InboundMessagesResource();
        ReflectionTestUtils.setField(inboundMessagesResource, "inboundMessagesRepository", inboundMessagesRepository);
        this.restInboundMessagesMockMvc = MockMvcBuilders.standaloneSetup(inboundMessagesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InboundMessages createEntity(EntityManager em) {
        InboundMessages inboundMessages = new InboundMessages();
        inboundMessages = new InboundMessages()
                .messageId(DEFAULT_MESSAGE_ID)
                .groupId(DEFAULT_GROUP_ID)
                .created_at(DEFAULT_CREATED_AT)
                .payload(DEFAULT_PAYLOAD);
        return inboundMessages;
    }

    @Before
    public void initTest() {
        inboundMessages = createEntity(em);
    }

    @Test
    @Transactional
    public void createInboundMessages() throws Exception {
        int databaseSizeBeforeCreate = inboundMessagesRepository.findAll().size();

        // Create the InboundMessages

        restInboundMessagesMockMvc.perform(post("/api/inbound-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inboundMessages)))
                .andExpect(status().isCreated());

        // Validate the InboundMessages in the database
        List<InboundMessages> inboundMessages = inboundMessagesRepository.findAll();
        assertThat(inboundMessages).hasSize(databaseSizeBeforeCreate + 1);
        InboundMessages testInboundMessages = inboundMessages.get(inboundMessages.size() - 1);
        assertThat(testInboundMessages.getMessageId()).isEqualTo(DEFAULT_MESSAGE_ID);
        assertThat(testInboundMessages.getGroupId()).isEqualTo(DEFAULT_GROUP_ID);
        assertThat(testInboundMessages.getCreated_at()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testInboundMessages.getPayload()).isEqualTo(DEFAULT_PAYLOAD);
    }

    @Test
    @Transactional
    public void getAllInboundMessages() throws Exception {
        // Initialize the database
        inboundMessagesRepository.saveAndFlush(inboundMessages);

        // Get all the inboundMessages
        restInboundMessagesMockMvc.perform(get("/api/inbound-messages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(inboundMessages.getId().intValue())))
                .andExpect(jsonPath("$.[*].messageId").value(hasItem(DEFAULT_MESSAGE_ID.toString())))
                .andExpect(jsonPath("$.[*].groupId").value(hasItem(DEFAULT_GROUP_ID.toString())))
                .andExpect(jsonPath("$.[*].created_at").value(hasItem(DEFAULT_CREATED_AT.toString())))
                .andExpect(jsonPath("$.[*].payload").value(hasItem(DEFAULT_PAYLOAD.toString())));
    }

    @Test
    @Transactional
    public void getInboundMessages() throws Exception {
        // Initialize the database
        inboundMessagesRepository.saveAndFlush(inboundMessages);

        // Get the inboundMessages
        restInboundMessagesMockMvc.perform(get("/api/inbound-messages/{id}", inboundMessages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(inboundMessages.getId().intValue()))
            .andExpect(jsonPath("$.messageId").value(DEFAULT_MESSAGE_ID.toString()))
            .andExpect(jsonPath("$.groupId").value(DEFAULT_GROUP_ID.toString()))
            .andExpect(jsonPath("$.created_at").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.payload").value(DEFAULT_PAYLOAD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInboundMessages() throws Exception {
        // Get the inboundMessages
        restInboundMessagesMockMvc.perform(get("/api/inbound-messages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInboundMessages() throws Exception {
        // Initialize the database
        inboundMessagesRepository.saveAndFlush(inboundMessages);
        int databaseSizeBeforeUpdate = inboundMessagesRepository.findAll().size();

        // Update the inboundMessages
        InboundMessages updatedInboundMessages = inboundMessagesRepository.findOne(inboundMessages.getId());
        updatedInboundMessages
                .messageId(UPDATED_MESSAGE_ID)
                .groupId(UPDATED_GROUP_ID)
                .created_at(UPDATED_CREATED_AT)
                .payload(UPDATED_PAYLOAD);

        restInboundMessagesMockMvc.perform(put("/api/inbound-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInboundMessages)))
                .andExpect(status().isOk());

        // Validate the InboundMessages in the database
        List<InboundMessages> inboundMessages = inboundMessagesRepository.findAll();
        assertThat(inboundMessages).hasSize(databaseSizeBeforeUpdate);
        InboundMessages testInboundMessages = inboundMessages.get(inboundMessages.size() - 1);
        assertThat(testInboundMessages.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
        assertThat(testInboundMessages.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testInboundMessages.getCreated_at()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testInboundMessages.getPayload()).isEqualTo(UPDATED_PAYLOAD);
    }

    @Test
    @Transactional
    public void deleteInboundMessages() throws Exception {
        // Initialize the database
        inboundMessagesRepository.saveAndFlush(inboundMessages);
        int databaseSizeBeforeDelete = inboundMessagesRepository.findAll().size();

        // Get the inboundMessages
        restInboundMessagesMockMvc.perform(delete("/api/inbound-messages/{id}", inboundMessages.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<InboundMessages> inboundMessages = inboundMessagesRepository.findAll();
        assertThat(inboundMessages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
