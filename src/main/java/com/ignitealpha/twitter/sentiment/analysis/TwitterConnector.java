package com.ignitealpha.twitter.sentiment.analysis;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterConnector {
    private TwitterConnectionUtils connectionUtils;
    private BlockingQueue<String> queue;
    private List<String> trackingTerms;
    private StatusesFilterEndpoint statusesFilterEndpoint;
    private Client client;

    public TwitterConnector() {
        connectionUtils = new TwitterConnectionUtils();
        queue = new LinkedBlockingQueue<String>(100);
        trackingTerms = new ArrayList<>();
        statusesFilterEndpoint = new StatusesFilterEndpoint();
        statusesFilterEndpoint.trackTerms(trackingTerms);
    }

    public TwitterConnector(int streamSize, List<String> trackingTerms) {
        connectionUtils = new TwitterConnectionUtils();
        queue = new LinkedBlockingQueue<String>(streamSize);
        this.trackingTerms = trackingTerms;
        statusesFilterEndpoint = new StatusesFilterEndpoint();
        statusesFilterEndpoint.trackTerms(trackingTerms);
    }

    public void connect() {
        client = new ClientBuilder().hosts(Constants.STREAM_HOST)
                .endpoint(statusesFilterEndpoint).authentication(connectionUtils.getAuthentication())
                .processor(new StringDelimitedProcessor(queue)).build();

        client.connect();
    }

    public void stop() {
        client.stop();
    }

    public BlockingQueue<String> getStream() {
        return queue;
    }
}
