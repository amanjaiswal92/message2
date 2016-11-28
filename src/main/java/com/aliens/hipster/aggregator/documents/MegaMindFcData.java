package com.aliens.hipster.aggregator.documents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by jayant on 22/9/16.
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MegaMindFcData {

    String fcId;
    Integer qty;
    Integer qt;
    }

