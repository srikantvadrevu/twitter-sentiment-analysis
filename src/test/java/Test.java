import com.ignitealpha.twitter.sentiment.analysis.KafkaProducerConnector;
import com.ignitealpha.twitter.sentiment.analysis.TwitterConnector;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class Test {
    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("iphone");
        TwitterConnector twitterConnector = new TwitterConnector(10, arrayList);
        twitterConnector.connect();
        BlockingQueue<String> queue = twitterConnector.getStream();

        for (int msgRead = 0; msgRead < 10; msgRead++) {
            try {
                System.out.println(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        twitterConnector.stop();
    }

    private void kafkaConnector() {
        KafkaProducerConnector producerConnector = new KafkaProducerConnector();
        producerConnector.connect();
        producerConnector.send("test twitter2");
        producerConnector.send("test twitter3");
        producerConnector.closeConnection();
    }
}
