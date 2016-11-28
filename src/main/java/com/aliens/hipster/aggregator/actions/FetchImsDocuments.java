package com.aliens.hipster.aggregator.actions;

import com.aliens.hipster.aggregator.config.UrlConfig;
import com.aliens.hipster.aggregator.documents.IMSDocument;
import com.aliens.hipster.aggregator.keycloak.KeyCloakUser;
import com.aliens.hipster.relayer.RestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.List;

/**
 * created by jayant on 21/9/16.
 */
@Component
@AllArgsConstructor
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Scope("prototype")
public class FetchImsDocuments  implements RestCall<List<IMSDocument>> {

    final RestUtil restUtil;
    final UrlConfig urlConfig;

    @Wither
    List<String>  eanList;


    static TypeReference<List<IMSDocument>> tRef = new TypeReference<List<IMSDocument>>() {};
    static ObjectMapper objectMapper = new ObjectMapper();

    public List<IMSDocument> invoke() throws Exception {

        List<IMSDocument> imsDocumentList=restUtil.withKeyCloakUser(KeyCloakUser.NODEUSER)
                .post(getUrl(),eanList,tRef);

        return imsDocumentList;
      }

    @Override
    public String getUrl() throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setPath(urlConfig.getBaseUrl()+urlConfig.getImsPath());
        return uriBuilder.build().toString();
    }
}
