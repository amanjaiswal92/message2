package com.aliens.hipster.aggregator.keycloak;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials {
  String username;
  String password;
  String clientId;
}
