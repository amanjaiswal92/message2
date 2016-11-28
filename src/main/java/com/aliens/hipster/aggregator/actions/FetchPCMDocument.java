package com.aliens.hipster.aggregator.actions;

import com.aliens.hipster.aggregator.config.UrlConfig;
import com.aliens.hipster.aggregator.documents.PCMResponse;
import com.aliens.hipster.aggregator.keycloak.KeyCloakUser;
import com.aliens.hipster.relayer.RestUtil;
import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.Set;

/**
 * Created by jayant on 21/9/16.
 */
@Component
@AllArgsConstructor
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Scope("prototype")
public class FetchPCMDocument  implements RestCall<PCMResponse> {

    @Wither
    Set<String> styleSet;


    final RestUtil restUtil;

    final UrlConfig urlConfig;

    public PCMResponse invoke() throws Exception
    {
        PCMResponse pcmResponse=restUtil.withKeyCloakUser(KeyCloakUser.NODEUSER)
                .get(getUrl(),PCMResponse.class);

        return pcmResponse;
    }

    @Override
    public String getUrl() throws URISyntaxException {
        Joiner joiner=Joiner.on(",");
        String styles=joiner.join(styleSet);
        URIBuilder uriBuilder = new URIBuilder();

        uriBuilder.setPath(urlConfig.getBaseUrl()+urlConfig.getPcmStylesMultiFetch());
        uriBuilder.setParameter("data",styles);

        return uriBuilder.build().toString();
    }
}
