package com.aliens.hipster.aggregator.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by jayant on 22/9/16.
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix = "spring.hosts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UrlConfig {
    String baseUrl;
    String imsPath;
    String pcmStylesMultiFetch;
    String megamindPath;
    String holmesPath;
    String eventDataPath;
}
