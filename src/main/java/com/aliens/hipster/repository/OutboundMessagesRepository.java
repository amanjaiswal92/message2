package com.aliens.hipster.repository;

import com.aliens.hipster.domain.OutboundMessages;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OutboundMessages entity.
 */
@SuppressWarnings("unused")
public interface OutboundMessagesRepository extends JpaRepository<OutboundMessages,Long> {

}
