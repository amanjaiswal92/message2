package com.aliens.hipster.aggregator.actions;

import com.aliens.hipster.aggregator.documents.*;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Provider;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by jayant on 21/9/16.
 */
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Scope("prototype")
public class ProcessImsEvents  {

    final Provider<FetchPCMDocument> fetchPCMDocumentProvider;
    final Provider<FetchMegaMindData> fetchMegaMindDataProvider;
    final Provider<PushToHolmes> pushToHolmes;

    @Wither
    List<IMSDocument> imsDocumentList;


    public String invoke() throws Exception {

        if(imsDocumentList.size()==0)return null;

        Map<String, List<IMSDocument>> imsMap = imsDocumentList
                .stream()
                .collect(Collectors.groupingBy(IMSDocument::getStyleId));

        log.info("Fetch PCM Doc");

        PCMResponse pcmResponse = fetchPCMDocumentProvider.get()
                .withStyleSet(imsMap.keySet())
                .invoke();

        List<MegaMindRequest> megaMindRequestList = Lists.newArrayList();

        for (String key : imsMap.keySet()) {
            PCMDocument pcmDocument = pcmResponse.getData().getOrDefault(key,null);

            if(pcmDocument==null)continue;
            List<IMSDocument> imsDocumentList1 = imsMap.get(key);


            imsDocumentList1.forEach(imsDocument ->
            {

                MegaMindRequest megaMindRequest = MegaMindRequest.builder()
                        .styleId(key)
                        .ean(imsDocument.getSkuId())
                        .brand(pcmDocument.getLegalBrandName())
                        .category(pcmDocument.getPcmArticleType())
                        .build();



                List<MegaMindFcData> megaMindFcDatas = Lists.newArrayList();
                imsDocument.getFCInventorys().forEach(fcInventory ->
                {

                    MegaMindFcData megaMindFcData=MegaMindFcData.builder()
                            .fcId(fcInventory.getFcId())
                            .qty(fcInventory.getQuantity()).build();

                    megaMindFcDatas.add(megaMindFcData);

                });

                megaMindRequest.setFcData(megaMindFcDatas);

                megaMindRequestList.add(megaMindRequest);
            });

        }

        if(megaMindRequestList.size()>0) {

            log.info("Calling megamind");

            List<MegaMindResponse> megaMindResponseList = fetchMegaMindDataProvider.get()
                    .withMegaMindRequestList(megaMindRequestList)
                    .invoke();

            Map<String, MegaMindResponse> megamindMap = megaMindResponseList.stream()
                    .collect(Collectors.toMap(MegaMindResponse::getEan, Function.identity()));


            imsMap.values().forEach(list -> {
                list.forEach(imsDocument -> {

                    if (megamindMap.containsKey(imsDocument.getSkuId())) {

                        MegaMindResponse mResponse = megamindMap.get(imsDocument.getSkuId());

                        imsDocument.setQuantity(mResponse.getOdinQty());

                        Map<String, MegaMindFcData> fcDataMap = mResponse.getFcData().stream().collect(
                                Collectors.toMap(MegaMindFcData::getFcId, Function.identity()));


                        imsDocument.getFCInventorys().forEach(fcInventory -> {

                            if (fcDataMap.containsKey(fcInventory.getFcId())) {
                                fcInventory.setQuantity(fcDataMap.get(fcInventory.getFcId()).getQt());
                            }
                        });

                    }
                });
            });
        }

        return pushToHolmes.get().withHolmesRequest(imsMap).invoke();

    }
}
