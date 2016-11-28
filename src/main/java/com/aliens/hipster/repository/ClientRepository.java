package com.aliens.hipster.repository;

import com.aliens.hipster.domain.Client;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Client entity.
 */
@SuppressWarnings("unused")
public interface ClientRepository extends JpaRepository<Client,Long> {

    Client getClientByName(String name);

    @Query("select c from Client c where c.state in (\'Idle\',\'Failed\') order by lastProcessedAt")
    List<Client> getEligibleClients(Pageable pageable);
}
