# twitter-sentiment-analysis

- Performed basic sentiment analysis on real time tweets using the Twitter Stream API.
- Used Apache Kafka to buffer the tweets before processing and then classified them into Positive/Negative/Neutral/Unable-to-Determine tweets.
- Programming Languages: Java, Python.
- Frameworks, Platforms and Libraries - [Flask](http://flask.pocoo.org/), [Apache Kafka](https://kafka.apache.org/), [textblob](https://textblob.readthedocs.io), [pyplot](https://matplotlib.org/tutorials/introductory/pyplot.html), [gson](https://github.com/google/gson)
- APIs - [HBC (Twitter Streams API)](https://github.com/twitter/hbc)

#### [Sentiment Analysis Client](https://github.com/srikantvadrevu/twitter-sentiment-analysis/blob/master/python/sentiment_analysis_client.py)
#### [Twitter Stream Connector](https://github.com/srikantvadrevu/twitter-sentiment-analysis/blob/master/src/main/java/com/ignitealpha/twitter/sentiment/analysis/TwitterConnector.java)
#### [Kafka Producer](https://github.com/srikantvadrevu/twitter-sentiment-analysis/blob/master/src/main/java/com/ignitealpha/twitter/sentiment/analysis/KafkaProducerConnector.java) 
