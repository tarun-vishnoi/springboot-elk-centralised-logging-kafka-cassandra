package com.api.kafka.consumer;

import java.io.IOException;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.google.gson.JsonParser;

@Component
public class TwitterConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TwitterConsumer.class);

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	@KafkaListener(topics = "twitter_feeds", groupId = "twitter_feeds_group", containerFactory = "kafkaListenerContainerFactory4Tweets")
	public void consumerTweets(String tweet) {

		String tweetId = new JsonParser().parse(tweet).getAsJsonObject().get("id_str").getAsString();

		IndexRequest indexRequest = new IndexRequest("twitter", null, tweetId).source(tweet, XContentType.JSON);
		try {
			IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
			LOGGER.info("TwitterConsumer ::  consumerTweets() :: Saved into elasticsearch " + indexResponse.getId());
		} catch (IOException e) {
			LOGGER.error("TwitterConsumer :: consumerTweets() :: ERROR " + e.getMessage());
		}
	}
}
