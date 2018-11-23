# -*- coding: utf-8 -*-
"""
Created on Thu Nov 22 23:04:13 2018

@author: Srikant Vadrevu
"""
from textblob import TextBlob as tb

blob = tb('good')

for sentence in blob.sentences:
    print(sentence.sentiment.polarity)