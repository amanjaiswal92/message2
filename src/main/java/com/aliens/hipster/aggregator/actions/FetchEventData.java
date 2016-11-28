package com.aliens.hipster.aggregator.actions;

import com.ailiens.common.Message;
import com.ailiens.common.RabbitMqMessageSender;
import com.aliens.hipster.aggregator.EventData;
import com.aliens.hipster.aggregator.config.UrlConfig;
import com.aliens.hipster.aggregator.documents.IMSDocument;
import com.aliens.hipster.aggregator.keycloak.KeyCloakUser;
import com.aliens.hipster.domain.OutboundMessages;
import com.aliens.hipster.relayer.RestUtil;
import com.aliens.hipster.aggregator.config.RabbitMqConfig;
import com.aliens.hipster.repository.OutboundMessagesRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Provider;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Scope("prototype")
@Slf4j
public class FetchEventData implements RestCall<Void>
{
	 @Wither
	 String eventId;

	 @Wither
	 String eventType;


	 final  ObjectMapper mapper;

	 final RestUtil restUtil;
	 final OutboundMessagesRepository outboundMessagesRepository;

	 final RabbitMqConfig rabbitMqConfig;
	 final UrlConfig urlConfig;
	 final RabbitMqMessageSender messageSender;
	 final Provider<FetchImsDocuments> fetchImsDocumentsProvider;

	 static TypeReference<List<EventData>> tRef = new TypeReference<List<EventData>>() {};

	  private void checkList(List list) throws Exception {

	  	if(list ==null || list.size()==0)
	  		throw new Exception("No data");
	  }

	private void saveInOutBound(Message message) {
		OutboundMessages outboundMessages = new OutboundMessages();
		outboundMessages.setGroupId(message.getGroupId());
		outboundMessages.setMessageId(message.getMessageId());
		outboundMessages.setPayload(message.getPayload());
		outboundMessagesRepository.save(outboundMessages);
	}

	 @Override
	 public Void invoke() throws Exception {

		 List<EventData> eventDataList= restUtil.withKeyCloakUser(KeyCloakUser.NODEUSER)
	                    .get(getUrl(), tRef);

		 checkList(eventDataList);

		 List<String> eanList = eventDataList.stream()
				 .map(eventData -> eventData.getEan())
				 .collect(Collectors.toList());

		 checkList(eanList);

		 log.info("Fetch IMS Doc");

		 List<IMSDocument> imsDocumentList = fetchImsDocumentsProvider.get()
				 .withEanList(eanList)
				 .invoke();

		 checkList(imsDocumentList);

		 Map<String, List<IMSDocument>> imsMap = imsDocumentList
				 .stream()
				 .collect(Collectors.groupingBy(IMSDocument::getStyleId));


		 for(String style : imsMap.keySet())
		 {
			 Message message = new Message();
			 message.setGroupId(style);
			 String payload = null;
			 try {
				 payload = mapper.writeValueAsString(imsMap.get(style));
			 } catch (JsonProcessingException e) {
				 e.printStackTrace();
			 }
			 message.setPayload(payload);
			 messageSender.sendMessage(message, rabbitMqConfig.getQueueName());
             saveInOutBound(message);
		 }
		 return null;
	    }

	 @Override
	  public String getUrl() throws URISyntaxException {
	        URIBuilder uriBuilder = new URIBuilder();
	        uriBuilder.setPath(urlConfig.getBaseUrl()+ urlConfig.getEventDataPath()+eventId);
	        return uriBuilder.build().toString();
	    }
}
