package com.aliens.hipster.aggregator.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by jayant on 27/9/16.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix = "spring.rabbit")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RabbitMqConfig {
    String host;
    String username;
    String password;
    Integer queueLimit;
    long timeout;
    long threadLifeTime;
    long sleepInterval;
    String queueName;
}
