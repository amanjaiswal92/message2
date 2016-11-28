package com.aliens.hipster.repository;

import com.aliens.hipster.domain.InboundMessages;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InboundMessages entity.
 */
@SuppressWarnings("unused")
public interface InboundMessagesRepository extends JpaRepository<InboundMessages,Long> {

}
