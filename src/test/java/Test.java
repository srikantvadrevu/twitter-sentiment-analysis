import com.google.gson.JsonParser;
import com.ignitealpha.twitter.sentiment.analysis.KafkaProducerConnector;
import com.ignitealpha.twitter.sentiment.analysis.TwitterConnector;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class Test {
    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("trump");
        TwitterConnector twitterConnector = new TwitterConnector(10000, arrayList);
        twitterConnector.connect();
        BlockingQueue<String> queue = twitterConnector.getStream();

        KafkaProducerConnector producerConnector = new KafkaProducerConnector();
        producerConnector.connect();


        for (int msgRead = 0; msgRead < 10000; msgRead++) {
            try {
                String tweet = queue.take();
                JsonParser jsonParser = new JsonParser();
                String tweetMessage = jsonParser.parse(tweet).getAsJsonObject().get("text").toString();
                System.out.println(tweetMessage);
                producerConnector.send(tweetMessage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        twitterConnector.stop();
        producerConnector.closeConnection();
    }

    private static void kafkaConnector() {
        KafkaProducerConnector producerConnector = new KafkaProducerConnector();
        producerConnector.connect();
        producerConnector.send("test twitter2");
        producerConnector.send("test twitter3");
        producerConnector.closeConnection();
    }
}
