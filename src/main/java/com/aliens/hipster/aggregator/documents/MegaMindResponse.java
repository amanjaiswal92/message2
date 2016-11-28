package com.aliens.hipster.aggregator.documents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jayant on 22/9/16.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MegaMindResponse implements Serializable {

    String styleId;
    String ean;
    String currentFcId;
    String brand;
    String category;
    Integer odinQty;
    String someQty;
    List<MegaMindFcData> fcData = Lists.newArrayList();
}

