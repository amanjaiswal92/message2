package com.aliens.hipster.aggregator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jayant on 20/9/16.
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventData implements Serializable {

    //ean event

    String ean;
    String styleId;
    String sapStyleId;
    String sapSkuId;
    String size;
    String sizeAttribute;
    int mrp;
    int tradeSp;
    String taxClass;
    String ageGroup;
    String pdpSize;
    String sapSize;
    String sapSizeAttribute;
    int eventId;
    List<String> fcInventory = Lists.newArrayList();


    //fc event

    String fcId;
    int quantity;
    String reserveQty;
    String source;
    String lastModifiedDate;
    String lastModifiedBy;
}
