package com.aliens.hipster.aggregator.actions;

import com.ailiens.common.Message;
import com.aliens.hipster.aggregator.documents.IMSDocument;
import com.aliens.hipster.domain.InboundMessages;
import com.aliens.hipster.repository.InboundMessagesRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Provider;
import java.util.List;

/**
 * Created by jayant on 29/9/16.
 */

@Component
@AllArgsConstructor
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Scope("prototype")
public class ProcessMsgEvent {

    final Provider<ProcessImsEvents> processImsEventsProvider;
    final ObjectMapper objectMapper;
    final InboundMessagesRepository inboundMessagesRepository;

    @Wither
    Message message;

    static TypeReference<List<IMSDocument>> tRef = new TypeReference<List<IMSDocument>>() {};

    private void saveInBound(Message message) {
        InboundMessages inboundMessages = new InboundMessages();
        inboundMessages.setGroupId(message.getGroupId());
        inboundMessages.setMessageId(message.getMessageId());
        inboundMessages.setPayload(message.getPayload());
        inboundMessagesRepository.save(inboundMessages);
    }

    public void invoke() throws Exception {

        log.info("Message Id {}",message.getMessageId());

        String payload= message.getPayload();

        List<IMSDocument> imsDocumentList=objectMapper.readValue(payload,tRef);

        processImsEventsProvider.get().withImsDocumentList(imsDocumentList).invoke();
        saveInBound(message);
    }
}
