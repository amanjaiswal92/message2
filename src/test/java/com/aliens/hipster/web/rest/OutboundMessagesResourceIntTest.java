package com.aliens.hipster.web.rest;

import com.aliens.hipster.HolmesaggregatorApp;
import com.aliens.hipster.domain.OutboundMessages;
import com.aliens.hipster.repository.OutboundMessagesRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OutboundMessagesResource REST controller.
 *
 * @see OutboundMessagesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HolmesaggregatorApp.class)
public class OutboundMessagesResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));
    private static final String DEFAULT_MESSAGE_ID = "AAAAA";
    private static final String UPDATED_MESSAGE_ID = "BBBBB";
    private static final String DEFAULT_GROUP_ID = "AAAAA";
    private static final String UPDATED_GROUP_ID = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_AT_STR = dateTimeFormatter.format(DEFAULT_CREATED_AT);
    private static final String DEFAULT_PAYLOAD = "AAAAA";
    private static final String UPDATED_PAYLOAD = "BBBBB";

    @Inject
    private OutboundMessagesRepository outboundMessagesRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOutboundMessagesMockMvc;

    private OutboundMessages outboundMessages;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OutboundMessagesResource outboundMessagesResource = new OutboundMessagesResource();
        ReflectionTestUtils.setField(outboundMessagesResource, "outboundMessagesRepository", outboundMessagesRepository);
        this.restOutboundMessagesMockMvc = MockMvcBuilders.standaloneSetup(outboundMessagesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutboundMessages createEntity(EntityManager em) {
        OutboundMessages outboundMessages = new OutboundMessages();
        outboundMessages = new OutboundMessages()
                .messageId(DEFAULT_MESSAGE_ID)
                .groupId(DEFAULT_GROUP_ID)
                .createdAt(DEFAULT_CREATED_AT)
                .payload(DEFAULT_PAYLOAD);
        return outboundMessages;
    }

    @Before
    public void initTest() {
        outboundMessages = createEntity(em);
    }

    @Test
    @Transactional
    public void createOutboundMessages() throws Exception {
        int databaseSizeBeforeCreate = outboundMessagesRepository.findAll().size();

        // Create the OutboundMessages

        restOutboundMessagesMockMvc.perform(post("/api/outbound-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outboundMessages)))
                .andExpect(status().isCreated());

        // Validate the OutboundMessages in the database
        List<OutboundMessages> outboundMessages = outboundMessagesRepository.findAll();
        assertThat(outboundMessages).hasSize(databaseSizeBeforeCreate + 1);
        OutboundMessages testOutboundMessages = outboundMessages.get(outboundMessages.size() - 1);
        assertThat(testOutboundMessages.getMessageId()).isEqualTo(DEFAULT_MESSAGE_ID);
        assertThat(testOutboundMessages.getGroupId()).isEqualTo(DEFAULT_GROUP_ID);
        assertThat(testOutboundMessages.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testOutboundMessages.getPayload()).isEqualTo(DEFAULT_PAYLOAD);
    }

    @Test
    @Transactional
    public void getAllOutboundMessages() throws Exception {
        // Initialize the database
        outboundMessagesRepository.saveAndFlush(outboundMessages);

        // Get all the outboundMessages
        restOutboundMessagesMockMvc.perform(get("/api/outbound-messages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(outboundMessages.getId().intValue())))
                .andExpect(jsonPath("$.[*].messageId").value(hasItem(DEFAULT_MESSAGE_ID.toString())))
                .andExpect(jsonPath("$.[*].groupId").value(hasItem(DEFAULT_GROUP_ID.toString())))
                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT_STR)))
                .andExpect(jsonPath("$.[*].payload").value(hasItem(DEFAULT_PAYLOAD.toString())));
    }

    @Test
    @Transactional
    public void getOutboundMessages() throws Exception {
        // Initialize the database
        outboundMessagesRepository.saveAndFlush(outboundMessages);

        // Get the outboundMessages
        restOutboundMessagesMockMvc.perform(get("/api/outbound-messages/{id}", outboundMessages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(outboundMessages.getId().intValue()))
            .andExpect(jsonPath("$.messageId").value(DEFAULT_MESSAGE_ID.toString()))
            .andExpect(jsonPath("$.groupId").value(DEFAULT_GROUP_ID.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT_STR))
            .andExpect(jsonPath("$.payload").value(DEFAULT_PAYLOAD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOutboundMessages() throws Exception {
        // Get the outboundMessages
        restOutboundMessagesMockMvc.perform(get("/api/outbound-messages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOutboundMessages() throws Exception {
        // Initialize the database
        outboundMessagesRepository.saveAndFlush(outboundMessages);
        int databaseSizeBeforeUpdate = outboundMessagesRepository.findAll().size();

        // Update the outboundMessages
        OutboundMessages updatedOutboundMessages = outboundMessagesRepository.findOne(outboundMessages.getId());
        updatedOutboundMessages
                .messageId(UPDATED_MESSAGE_ID)
                .groupId(UPDATED_GROUP_ID)
                .createdAt(UPDATED_CREATED_AT)
                .payload(UPDATED_PAYLOAD);

        restOutboundMessagesMockMvc.perform(put("/api/outbound-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOutboundMessages)))
                .andExpect(status().isOk());

        // Validate the OutboundMessages in the database
        List<OutboundMessages> outboundMessages = outboundMessagesRepository.findAll();
        assertThat(outboundMessages).hasSize(databaseSizeBeforeUpdate);
        OutboundMessages testOutboundMessages = outboundMessages.get(outboundMessages.size() - 1);
        assertThat(testOutboundMessages.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
        assertThat(testOutboundMessages.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testOutboundMessages.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOutboundMessages.getPayload()).isEqualTo(UPDATED_PAYLOAD);
    }

    @Test
    @Transactional
    public void deleteOutboundMessages() throws Exception {
        // Initialize the database
        outboundMessagesRepository.saveAndFlush(outboundMessages);
        int databaseSizeBeforeDelete = outboundMessagesRepository.findAll().size();

        // Get the outboundMessages
        restOutboundMessagesMockMvc.perform(delete("/api/outbound-messages/{id}", outboundMessages.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OutboundMessages> outboundMessages = outboundMessagesRepository.findAll();
        assertThat(outboundMessages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
