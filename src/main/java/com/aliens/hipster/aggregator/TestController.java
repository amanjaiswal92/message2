package com.aliens.hipster.aggregator;

import com.aliens.hipster.aggregator.actions.FetchImsDocuments;
import com.aliens.hipster.aggregator.actions.FetchMegaMindData;
import com.aliens.hipster.aggregator.actions.FetchPCMDocument;
import com.aliens.hipster.aggregator.documents.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Provider;
import java.util.List;
import java.util.Set;

/**
 * Created by jayant on 23/9/16.
 */

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TestController {

    final Provider<FetchPCMDocument> fetchPCMDocumentProvider;
    final Provider<FetchMegaMindData> fetchMegaMindDataProvider;
    final Provider<FetchImsDocuments> fetchImsDocumentsProvider;


    @RequestMapping(value = "/pcm",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> testPCM() throws Exception {


        Set<String> styles= Sets.newConcurrentHashSet();
        styles.add("AP2MQ8NK0A");
        PCMResponse pcmResponse=fetchPCMDocumentProvider.get().withStyleSet(styles).invoke();

        return ResponseEntity.ok()
                .body(pcmResponse);
    }

    @RequestMapping(value = "/megamind",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> testMegamind() throws Exception {

        List<MegaMindRequest> megaMindRequestList = Lists.newArrayList();

        megaMindRequestList.add(MegaMindRequest.builder()
                .styleId("AP2MQ8NK0A")
                .ean("65616063606466")
                .category("category")
                .brand("Gap")
                .fcDataSingle(MegaMindFcData.builder().fcId("0000888888").qty(100).build())
                .build());

        List<MegaMindResponse> megaMindResponseList=fetchMegaMindDataProvider.get()
                .withMegaMindRequestList(megaMindRequestList).invoke();

        return ResponseEntity.ok()
                .body(megaMindResponseList);
    }

    @RequestMapping(value = "/ims",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> testIMS() throws Exception {


        List<IMSDocument> imsDocumentList =fetchImsDocumentsProvider.get().withEanList(null).invoke();

        return ResponseEntity.ok()
                .body(imsDocumentList);
    }
}
