package com.aliens.hipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aliens.hipster.domain.InboundMessages;

import com.aliens.hipster.repository.InboundMessagesRepository;
import com.aliens.hipster.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing InboundMessages.
 */
@RestController
@RequestMapping("/api")
public class InboundMessagesResource {

    private final Logger log = LoggerFactory.getLogger(InboundMessagesResource.class);
        
    @Inject
    private InboundMessagesRepository inboundMessagesRepository;

    /**
     * POST  /inbound-messages : Create a new inboundMessages.
     *
     * @param inboundMessages the inboundMessages to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inboundMessages, or with status 400 (Bad Request) if the inboundMessages has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/inbound-messages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InboundMessages> createInboundMessages(@RequestBody InboundMessages inboundMessages) throws URISyntaxException {
        log.debug("REST request to save InboundMessages : {}", inboundMessages);
        if (inboundMessages.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("inboundMessages", "idexists", "A new inboundMessages cannot already have an ID")).body(null);
        }
        InboundMessages result = inboundMessagesRepository.save(inboundMessages);
        return ResponseEntity.created(new URI("/api/inbound-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("inboundMessages", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /inbound-messages : Updates an existing inboundMessages.
     *
     * @param inboundMessages the inboundMessages to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inboundMessages,
     * or with status 400 (Bad Request) if the inboundMessages is not valid,
     * or with status 500 (Internal Server Error) if the inboundMessages couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/inbound-messages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InboundMessages> updateInboundMessages(@RequestBody InboundMessages inboundMessages) throws URISyntaxException {
        log.debug("REST request to update InboundMessages : {}", inboundMessages);
        if (inboundMessages.getId() == null) {
            return createInboundMessages(inboundMessages);
        }
        InboundMessages result = inboundMessagesRepository.save(inboundMessages);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("inboundMessages", inboundMessages.getId().toString()))
            .body(result);
    }

    /**
     * GET  /inbound-messages : get all the inboundMessages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of inboundMessages in body
     */
    @RequestMapping(value = "/inbound-messages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InboundMessages> getAllInboundMessages() {
        log.debug("REST request to get all InboundMessages");
        List<InboundMessages> inboundMessages = inboundMessagesRepository.findAll();
        return inboundMessages;
    }

    /**
     * GET  /inbound-messages/:id : get the "id" inboundMessages.
     *
     * @param id the id of the inboundMessages to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inboundMessages, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/inbound-messages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InboundMessages> getInboundMessages(@PathVariable Long id) {
        log.debug("REST request to get InboundMessages : {}", id);
        InboundMessages inboundMessages = inboundMessagesRepository.findOne(id);
        return Optional.ofNullable(inboundMessages)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /inbound-messages/:id : delete the "id" inboundMessages.
     *
     * @param id the id of the inboundMessages to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/inbound-messages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInboundMessages(@PathVariable Long id) {
        log.debug("REST request to delete InboundMessages : {}", id);
        inboundMessagesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("inboundMessages", id.toString())).build();
    }

}
