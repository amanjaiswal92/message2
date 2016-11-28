package com.aliens.hipster.relayer;

import com.aliens.hipster.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Provider;

/**
 * Created by jayant on 15/9/16.
 */
@Component
@Slf4j
public class Scheduler {

    @Autowired
    Provider<PushMessageAction> pushMessageActionProvider;

    @Autowired
    ClientRepository clientRepository;


    PageRequest pageRequest = new PageRequest(0,1);

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void poll() throws Exception {

        clientRepository.getEligibleClients(pageRequest).stream()
            .forEach(client -> {
                try {
                    log.info(client.getName());
                    client.setState("Processing..");  //force commit here
                    clientRepository.flush();
                    pushMessageActionProvider.get().withClient(client).invoke();
                }
                catch(Exception e)
                {
                    log.error("Error while polling for client {} {}",client.getName(), ExceptionUtils.getMessage(e));
                }
            });
    }
}
