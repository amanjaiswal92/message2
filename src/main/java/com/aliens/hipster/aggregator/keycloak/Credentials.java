package com.aliens.hipster.aggregator.keycloak;

import com.aliens.hipster.aggregator.config.KeyCloakConfig;
import com.aliens.hipster.relayer.RestUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Singleton
@Component
public class Credentials {

	@Autowired
	KeyCloakConfig keyCloakConfig;

	private LoadingCache<KeyCloakUser, String> cache = CacheBuilder.newBuilder().maximumSize(100)
			.expireAfterWrite(10, TimeUnit.MINUTES).build(new CacheLoader<KeyCloakUser, String>() {
				@Override
				public String load(KeyCloakUser key) throws Exception {
					return generateAccessToken(key);
				}
			});
	private RestUtil restUtil = new RestUtil();

	public void updateKey(KeyCloakUser user) {
		cache.refresh(user);
	}

	public String getAccessToken(KeyCloakUser user) throws ExecutionException {
		return cache.get(user);
	}

	private String generateAccessToken(KeyCloakUser keyCloakUser) throws Exception {

		log.info("Getting new Token for {}", keyCloakUser.toString());
		UserCredentials userCredentials = keyCloakConfig.getMap().getOrDefault(keyCloakUser, null);

		if (userCredentials == null) {
			log.error("Keycoak user not found");
			throw new Exception("Keycoak user not found");
		}
		HttpResponse<KeyCloakResponse> httpResponse = Unirest.post(keyCloakConfig.getKeycloakGetAccessTokenUrl())
				.header("content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("Authorization", keyCloakConfig.getAuthorization())
				.field("username", userCredentials.getUsername())
				.field("password", userCredentials.getPassword())
				.field("client", userCredentials.getClientId()).asObject(KeyCloakResponse.class);

		restUtil.checkStatus(httpResponse);

		return httpResponse.getBody().getAccessToken();
	}

}
