package com.process.kafka;

import java.util.Date;
import java.util.Random;

import org.apache.kafka.clients.consumer.ConsumerRecord;
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
import com.process.pojo.Tweet;
import com.process.pojo.Tweets;
import com.process.repo.TweetsCassandraRepo;
import com.process.repo.TweetsPostgresqlRepo;

@Component
public class TweetsConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TweetsConsumer.class);

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	@Autowired
	private TweetsCassandraRepo tweetsCassandraRepo;

	@Autowired
	private TweetsPostgresqlRepo tweetsPostgresqlRepo;

	// optional containerFactory
	@KafkaListener(topics = "twitter_feeds", groupId = "twitter_feeds_group", containerFactory = "kafkaListenerContainerFactory")
	public void consumerTweets(ConsumerRecord<String, String> tweet) {
		if (tweet != null) {
			String tweetId = new JsonParser().parse(tweet.value()).getAsJsonObject().get("id_str").getAsString();

			// saving to elasticsearch

			IndexRequest indexRequest = new IndexRequest("twitter", null, tweetId).source(tweet, XContentType.JSON);
			try {
				IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

				// saving to cassandra

				Tweet tweet4Cassandra = new Tweet();
				tweet4Cassandra.setCreatedDate(new Date());
				tweet4Cassandra.setId(new Random().nextInt());
				tweet4Cassandra.setTweetId(tweetId.trim());
				tweet4Cassandra.setTweet(tweet.value().trim());
				tweetsCassandraRepo.save(tweet4Cassandra);

				// saving to postgreSQL

				Tweets tweet4Postgresql = new Tweets();
				tweet4Postgresql.setCreatedDate(new Date());
				tweet4Postgresql.setTweetId(tweetId.trim());
				tweet4Postgresql.setTweet(tweet.value().trim());
				tweetsPostgresqlRepo.save(tweet4Postgresql);

				LOGGER.info("TweetsConsumer :: consumerTweets() :: topic = " + tweet.topic() + ", partition = "
						+ tweet.partition() + ", Offset = " + tweet.offset());
			} catch (Exception e) {
				LOGGER.error("TweetsConsumer :: consumerTweets() :: ERROR" + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
