# -*- coding: utf-8 -*-
"""
Created on Thu Nov 22 23:04:13 2018

@author: Srikant Vadrevu
"""
from flask import Flask, jsonify, render_template
from textblob import TextBlob as tb
import re 
from kafka import KafkaConsumer
import json

app = Flask(__name__)

# Removes the links and special characters from the provided tweet.
def clean_tweet(tweet):
    return ' '.join(re.sub("(@[A-Za-z0-9]+)|([^0-9A-Za-z \t])|(\w+:\/\/\S+)", " ", tweet).split())

# Fetches the polarity values for the provided tweet.    
def polarity(tweet):
    blob = tb(clean_tweet(tweet))
    for sentence in blob.sentences:
        return sentence.sentiment.polarity

# Fetches sentiment for the given polarity value and updated the values to be plotted.
def get_sentiment(polarity, values):
    polarity = float(polarity)
    if polarity < 0:
        if polarity < -0.4:
            values[1] = values[1] + 1
            return 'Negative'
        values[3] = values[3] + 1
        return 'Not Sure'
    if polarity == 0.0:
        values[2] = values[2] + 1
        return 'Neutral'
    if polarity < 0.4:
        values[3] = values[3] + 1
        return 'Not Sure'
    values[0] = values[0] + 1
    return 'Positive'

# Initializing the kafka consumer.
consumer = KafkaConsumer(
        'twitter-sentiment-analysis', 
        auto_offset_reset='earliest',
        bootstrap_servers=['localhost:9092'], 
        api_version=(0, 10), 
        consumer_timeout_ms=1000
        )

# Renders an image visualizing the sentiment anaysis in a pie chart.
@app.route('/')
def index():
    import matplotlib.pyplot as plt
    import io
    import base64

    response = []
    
    # Graph labels and initializing the values to be plotted
    labels = 'Positive', 'Negative', 'Neutral', 'Not Sure'
    values = [0, 0, 0, 0]
    explode = (0, 0, 0, 0)

    for msg in consumer:
        tweet = msg.value.decode("utf-8")
        polarity_value = polarity(tweet)
        if polarity_value != None:
            sentiment = get_sentiment(polarity_value, values)
            response.append(json.dumps({'tweet': tweet, 'sentiment': sentiment}))
    
    # Plotting the graph
    fig1, ax1 = plt.subplots()
    ax1.pie(values, explode=explode, labels=labels, autopct='%1.1f%%',
        shadow=True, startangle=90)
    ax1.axis('equal')  # Equal aspect ratio ensures that pie is drawn as a circle.        
    
    #plt.show() # To open the graph in a new window
    
    img = io.BytesIO()  # create the buffer
    plt.savefig(img, format='png')  # save image to the buffer
    img.seek(0) 
    
    graph_url = base64.b64encode(img.getvalue()).decode()
    plt.close()
   
    #return jsonify(response) # Returns json response format {tweet: 'tweet text', sentiment: 'sentiment value'}
    return render_template('index.html',graph1 = 'data:image/png;base64,{}'.format(graph_url))

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=False)