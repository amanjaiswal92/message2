package com.aliens.hipster.aggregator.documents;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jayant on 22/9/16.
 */

@Data
@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MegaMindRequest  {

    String styleId;
    String ean;
    String brand;
    String category;

    @Singular("fcDataSingle")
    List<MegaMindFcData> fcData= new ArrayList<MegaMindFcData>();

    public void addFcData(MegaMindFcData megaMindFcData)
    {
        fcData.add(megaMindFcData);
    }
}
