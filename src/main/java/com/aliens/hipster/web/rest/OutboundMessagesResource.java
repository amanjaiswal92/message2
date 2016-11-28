package com.aliens.hipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aliens.hipster.domain.OutboundMessages;

import com.aliens.hipster.repository.OutboundMessagesRepository;
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
 * REST controller for managing OutboundMessages.
 */
@RestController
@RequestMapping("/api")
public class OutboundMessagesResource {

    private final Logger log = LoggerFactory.getLogger(OutboundMessagesResource.class);
        
    @Inject
    private OutboundMessagesRepository outboundMessagesRepository;

    /**
     * POST  /outbound-messages : Create a new outboundMessages.
     *
     * @param outboundMessages the outboundMessages to create
     * @return the ResponseEntity with status 201 (Created) and with body the new outboundMessages, or with status 400 (Bad Request) if the outboundMessages has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/outbound-messages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OutboundMessages> createOutboundMessages(@RequestBody OutboundMessages outboundMessages) throws URISyntaxException {
        log.debug("REST request to save OutboundMessages : {}", outboundMessages);
        if (outboundMessages.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("outboundMessages", "idexists", "A new outboundMessages cannot already have an ID")).body(null);
        }
        OutboundMessages result = outboundMessagesRepository.save(outboundMessages);
        return ResponseEntity.created(new URI("/api/outbound-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("outboundMessages", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /outbound-messages : Updates an existing outboundMessages.
     *
     * @param outboundMessages the outboundMessages to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated outboundMessages,
     * or with status 400 (Bad Request) if the outboundMessages is not valid,
     * or with status 500 (Internal Server Error) if the outboundMessages couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/outbound-messages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OutboundMessages> updateOutboundMessages(@RequestBody OutboundMessages outboundMessages) throws URISyntaxException {
        log.debug("REST request to update OutboundMessages : {}", outboundMessages);
        if (outboundMessages.getId() == null) {
            return createOutboundMessages(outboundMessages);
        }
        OutboundMessages result = outboundMessagesRepository.save(outboundMessages);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("outboundMessages", outboundMessages.getId().toString()))
            .body(result);
    }

    /**
     * GET  /outbound-messages : get all the outboundMessages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of outboundMessages in body
     */
    @RequestMapping(value = "/outbound-messages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OutboundMessages> getAllOutboundMessages() {
        log.debug("REST request to get all OutboundMessages");
        List<OutboundMessages> outboundMessages = outboundMessagesRepository.findAll();
        return outboundMessages;
    }

    /**
     * GET  /outbound-messages/:id : get the "id" outboundMessages.
     *
     * @param id the id of the outboundMessages to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the outboundMessages, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/outbound-messages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OutboundMessages> getOutboundMessages(@PathVariable Long id) {
        log.debug("REST request to get OutboundMessages : {}", id);
        OutboundMessages outboundMessages = outboundMessagesRepository.findOne(id);
        return Optional.ofNullable(outboundMessages)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /outbound-messages/:id : delete the "id" outboundMessages.
     *
     * @param id the id of the outboundMessages to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/outbound-messages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOutboundMessages(@PathVariable Long id) {
        log.debug("REST request to delete OutboundMessages : {}", id);
        outboundMessagesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("outboundMessages", id.toString())).build();
    }

}
