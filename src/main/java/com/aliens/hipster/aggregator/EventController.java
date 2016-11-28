package com.aliens.hipster.aggregator;

import com.ailiens.common.Message;
import com.ailiens.common.RabbitMqMessageSender;
import com.aliens.hipster.aggregator.actions.FetchEventData;
import com.aliens.hipster.aggregator.actions.ProcessImsEvents;
import com.aliens.hipster.aggregator.actions.ProcessMsgBulkEvents;
import com.aliens.hipster.aggregator.actions.ProcessMsgEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Provider;
import java.util.List;

/**
 * Created by jayant on 20/9/16.
 */


@RestController
@RequestMapping("/event")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class EventController {

    final Provider<ProcessImsEvents> processImsEventsProvider;
    final Provider<ProcessMsgEvent> processMsgEventProvider;
    final Provider<ProcessMsgBulkEvents> processMsgBulkEventsProvider;

    final Provider<FetchEventData> fetchEventDataProvider;
    final RabbitMqMessageSender messageSender;




    @RequestMapping(value = "/subscriber/{eventType}",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> eanSubscriber(@PathVariable("eventType") String eventType , @RequestParam("eventId") String eventId) throws Exception {

    	fetchEventDataProvider.get().withEventId(eventId).withEventType(eventType).invoke();

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/msgEvent",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> processMsgEvent(@RequestBody Message message) throws Exception {

        processMsgEventProvider.get().withMessage(message).invoke();

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/bulk/msgEvent",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> processMsgBulkEvent(@RequestBody List<Message> messageList) throws Exception {

        processMsgBulkEventsProvider.get().withMessageList(messageList).invoke();

        return ResponseEntity.ok().build();
    }
}
