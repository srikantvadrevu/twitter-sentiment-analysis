package com.ignitealpha.twitter.sentiment.analysis;

import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

class TwitterConnectionUtils {
    private Authentication authentication;

    Authentication getAuthentication() {
        if (authentication == null) {
            // Replace the environment variables to access with your credentials.
            authentication = new OAuth1(
                    System.getenv("TWITTER_CONSUMER_KEY"),
                    System.getenv("TWITTER_CONSUMER_SECRET"),
                    System.getenv("TWITTER_ACCESS_TOKEN"),
                    System.getenv("TWITTER_ACCESS_SECRET")
            );
        }

        return authentication;
    }
}
