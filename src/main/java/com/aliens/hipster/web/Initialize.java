package com.aliens.hipster.web;

import com.ailiens.common.PoolType;
import com.ailiens.common.RabbitMqConnectionManager;
import com.aliens.hipster.relayer.RestUtil;
import com.aliens.hipster.aggregator.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by jayant on 30/9/16.
 */

@Component
@Slf4j
public class Initialize {

    @Autowired
    RabbitMqConfig rabbitMqConfig;

    final String CLUSTER_NAME="holmes";

    @PostConstruct
    public void setup() throws IOException, TimeoutException {


        RabbitMqConnectionManager.setPoolType(PoolType.EAGER);
        RabbitMqConnectionManager.setPoolSize(2);
        RabbitMqConnectionManager.createConnectionPool(CLUSTER_NAME,rabbitMqConfig.getHost(),rabbitMqConfig.getUsername(),rabbitMqConfig.getPassword());

        RabbitMqConnectionManager.setDefaultCluster(CLUSTER_NAME);
        RestUtil.setupUnirest();

        log.info("Setup done");
    }
}
