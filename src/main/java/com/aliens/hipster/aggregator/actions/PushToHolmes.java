package com.aliens.hipster.aggregator.actions;

import com.aliens.hipster.aggregator.config.UrlConfig;
import com.aliens.hipster.aggregator.documents.IMSDocument;
import com.aliens.hipster.aggregator.keycloak.KeyCloakUser;
import com.aliens.hipster.relayer.RestUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * Created by jayant on 22/9/16.
 */


@Component
@Scope("prototype")
@AllArgsConstructor
@NoArgsConstructor
public class PushToHolmes implements RestCall<String> {

	@Wither
	Map<String,List<IMSDocument>> holmesRequest;

	@Autowired
	RestUtil restUtil;

	@Autowired
	UrlConfig urlConfig;

	public String invoke() throws Exception {
	    return restUtil.withKeyCloakUser(KeyCloakUser.NODEUSER).post(getUrl(),holmesRequest,String.class);

	}

	@Override
	public String getUrl() throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder();
		uriBuilder.setPath(urlConfig.getBaseUrl()+urlConfig.getHolmesPath());
        return uriBuilder.build().toString();
	}
}
