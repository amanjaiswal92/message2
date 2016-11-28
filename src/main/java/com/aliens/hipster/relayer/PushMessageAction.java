package com.aliens.hipster.relayer;

import com.ailiens.common.RabbitMqMessageSender;
import com.aliens.hipster.aggregator.actions.FetchEventData;
import com.aliens.hipster.domain.Client;
import com.aliens.hipster.repository.ClientRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Provider;
import java.util.List;

/**
 * Created by jayant on 15/9/16.
 */
@Component
@Scope("prototype")
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class PushMessageAction {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    RestUtil restUtil;

    @Autowired
    Provider<FetchEventData> fetchEventDataProvider;

    @Autowired
    RabbitMqMessageSender messageSender;

    @Wither
    private Client client;

    static ObjectMapper mapper = new ObjectMapper();


    public void updateClientState(String state)
    {
        client.setState(state);
    }


    public void invoke() throws Exception {

        try {
            Thread.sleep(10000);

            URIBuilder uriBuilder = new URIBuilder(client.getUri());
            uriBuilder.setPath(client.getReadOffset().toString());
            uriBuilder.setParameter("size", client.getPageSize().toString());
            String uri = uriBuilder.build().toString();

            log.info(uri);

            String responseStr = restUtil.get(uri, String.class);

            TypeReference<List<Integer>> tRef = new TypeReference<List<Integer>>() {
            };

            List<Integer> eventList = mapper.readValue(responseStr, tRef);


            for (Integer eventId : eventList)
            {

                    fetchEventDataProvider.get().withEventId(String.valueOf(eventId)).invoke();

                client.setReadOffset(eventId);
            }

            updateClientState("Idle");

        }
        catch (Exception e)
        {
            log.info("Got error while processing Client {} Error {}",client.getName(), ExceptionUtils.getMessage(e));
            updateClientState("Failed");
            throw e;
        }
        finally {
             client.updateProcessedTime();
        }
    }
}
