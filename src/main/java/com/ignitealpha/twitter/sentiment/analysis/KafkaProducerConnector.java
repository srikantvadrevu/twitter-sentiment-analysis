package com.ignitealpha.twitter.sentiment.analysis;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

public class KafkaProducerConnector {
    private KafkaProducer producer;
    private static final String TOPIC_NAME = "twitter-sentiment-analysis";
    private String url = "18.218.28.74:9092";

    public KafkaProducerConnector() {
    }

    public KafkaProducerConnector(String serverUrl) {
        this.url = serverUrl;
    }

    public void send(String message) {
        producer.send(new ProducerRecord<String, String>(TOPIC_NAME, String.valueOf(1), message),
                new Callback() {
                    public void onCompletion(RecordMetadata metadata, Exception e) {
                        if (e != null)
                            e.printStackTrace();
                        System.out.println("The offset of the record we just sent is: " + metadata.offset());
                    }
                });
        producer.flush();
    }

    public void connect() {
        producer = new KafkaProducer<String, String>(getProducerConfig());
    }

    public void closeConnection() {
        producer.close();
    }

    private Properties getProducerConfig() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", url);

        // Set acknowledgements for producer requests.
        properties.put("acks", "all");

        // number of times producer retries
        properties.put("retries", 2);

        // buffer size in config
        properties.put("batch.size", 16384);

        // no of requests less than 0
        properties.put("linger.ms", 1);

        // The buffer.memory controls the total amount of memory available to
        // the producer for buffering.
        properties.put("buffer.memory", 33554432);

        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        properties.put("metadata.broker.list", url);

        properties.put("serializer.class", "kafka.serializer.StringEncoder");

        return properties;
    }
}
