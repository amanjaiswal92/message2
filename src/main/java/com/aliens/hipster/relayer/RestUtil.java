package com.aliens.hipster.relayer;


import com.aliens.hipster.aggregator.keycloak.Credentials;
import com.aliens.hipster.aggregator.keycloak.KeyCloakUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by jayant on 13/9/16.
 */

@Component
@Scope("prototype")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Slf4j
public class RestUtil {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Credentials credentials;

    @Wither
    Map<String,String> headers = new HashMap<>();

    @Wither
    KeyCloakUser keyCloakUser;

    public RestUtil()
    {
        headers.put("content-type",MediaType.APPLICATION_JSON_VALUE);
        keyCloakUser=null;
    }

    public  <T> T get(String url,  Class <? extends T> responseClass) throws Exception {

        checkKeyCloak();

        log.info(url);

        HttpResponse<T> response = Unirest.get(url)
                .headers(headers)
            .asObject(responseClass);

        checkStatus(response);

        return response.getBody();
    }

    public  <T> List<T> get(String url,  TypeReference<List<T>> typeReference) throws Exception {

    	 String responseStr= get(url,String.class);
         List<T> responseList=objectMapper.readValue(responseStr,typeReference);
         return responseList;
    }

    public  <T> T post(String url, Object payload, Class <? extends T> responseClass) throws Exception {

        checkKeyCloak();
        log.info(url);
        log.info(payload.toString());
        HttpResponse<T> response = Unirest.post(url)
                .headers(headers)
                .body(payload)
                .asObject(responseClass);

        checkStatus(response);

        return response.getBody();
    }

    public  <T> T put(String url, Object payload, Class <? extends T> responseClass) throws Exception {

        checkKeyCloak();
        log.info(url);
        log.info(payload.toString());
        HttpResponse<T> response = Unirest.put(url)
                .headers(headers)
                .body(payload)
                .asObject(responseClass);

        checkStatus(response);

        return response.getBody();
    }

    public  <T> List<T> post(String url, Object payload, TypeReference<List<T>> typeReference) throws Exception {

        String responseStr= post(url,payload,String.class);
        List<T> responseList=objectMapper.readValue(responseStr,typeReference);
        return responseList;
    }

    public void checkKeyCloak() throws ExecutionException {
        if(keyCloakUser!=null)
        {
            String auth= credentials.getAccessToken(keyCloakUser);
            headers.put("Authorization","Bearer "+auth);
        }
    }

    public  void checkStatus(HttpResponse response) throws Exception {

        log.info(response.getBody().toString());

        if(response.getStatus()==401 && keyCloakUser!=null)
        {
            credentials.updateKey(keyCloakUser);
        }

        if(response.getStatus()/100!=2)
            throw new Exception("Got error while calling: "+response.getStatusText());

    }

    public static void setupUnirest()
    {
        log.info("Unirest setup done");
        Unirest.setObjectMapper(new com.mashape.unirest.http.ObjectMapper() {
            private ObjectMapper jacksonObjectMapper
                = new ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


}
