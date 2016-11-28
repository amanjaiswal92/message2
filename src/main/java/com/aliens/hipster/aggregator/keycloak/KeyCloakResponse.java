package com.aliens.hipster.aggregator.keycloak;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Created by jayant on 14/9/16.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeyCloakResponse {
  String accessToken;
}
