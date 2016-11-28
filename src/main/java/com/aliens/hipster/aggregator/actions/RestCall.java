package com.aliens.hipster.aggregator.actions;


import java.net.URISyntaxException;

/**
 * Created by jayant on 22/9/16.
 */
public interface RestCall<T> {

    T invoke() throws Exception,java.io.IOException;

    String getUrl() throws URISyntaxException;
}
