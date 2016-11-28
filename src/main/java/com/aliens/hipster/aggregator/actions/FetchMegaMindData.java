package com.aliens.hipster.aggregator.actions;

import com.aliens.hipster.aggregator.config.UrlConfig;
import com.aliens.hipster.aggregator.documents.MegaMindRequest;
import com.aliens.hipster.aggregator.documents.MegaMindResponse;
import com.aliens.hipster.aggregator.keycloak.KeyCloakUser;
import com.aliens.hipster.relayer.RestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
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
 * Created by jayant on 22/9/16.
 */
@Component
@AllArgsConstructor
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Scope("prototype")
public class FetchMegaMindData implements RestCall<List<MegaMindResponse>> {

    @Wither
    List<MegaMindRequest> megaMindRequestList;

    final RestUtil restUtil;

    final UrlConfig urlConfig;

    static TypeReference<List<MegaMindResponse>> tRef = new TypeReference<List<MegaMindResponse>>() {};

    @Override
    public List<MegaMindResponse> invoke() throws Exception {

        List<MegaMindResponse> megaMindResponseList= restUtil.withKeyCloakUser(KeyCloakUser.NODEUSER)
                    .post(getUrl(),megaMindRequestList,tRef);

        return megaMindResponseList;
    }

    @Override
    public String getUrl() throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setPath(urlConfig.getBaseUrl()+urlConfig.getMegamindPath());
        return uriBuilder.build().toString();
    }
}
