package com.api.kafka.producer;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

@Component
public class TwitterProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TwitterProducer.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	private String consumerKey = "cSyVK4awkEQt1AGPiHYQKlsC0";
	private String consumerSecret = "ynstULA9Vi7l8Jqw7yoHmIh17RfQ0SEnAh2RJVz0QH79m1WGvY";
	private String token = "1280007926057299969-rejAF8dMPJOZW0lT3nJD6qq3TBXe5q";
	private String secret = "BKiBHX8RokAlkIPALF2WQ246KIzzS6KUis94gPaQcLq3A";

	public void produceTweets() {
		/**
		 * Set up your blocking queues: Be sure to size these properly based on expected
		 * TPS of your stream
		 */
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
		Client client = createTwitterClient(msgQueue);
		client.connect();

		// on a different thread, or multiple different threads....
		while (!client.isDone()) {
			String tweet = null;
			try {
				tweet = msgQueue.poll(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				LOGGER.error("TwitterProducer :: produceTweets() :: ERROR" + e.getMessage());
				client.stop();
			}
			if (tweet != null) {
				kafkaTemplate.send(new ProducerRecord<String, String>("twitter_feeds", tweet));
			}
		}
	}

	private Client createTwitterClient(BlockingQueue<String> msgQueue) {

		/**
		 * Declare the host you want to connect to, the endpoint, and authentication
		 * (basic auth or oauth)
		 */
		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

		List<String> terms = Lists.newArrayList("india", "china", "corona", "army");
		hosebirdEndpoint.trackTerms(terms);

		Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token, secret);

		ClientBuilder builder = new ClientBuilder().name("Hosebird-Client-01").hosts(hosebirdHosts)
				.authentication(hosebirdAuth).endpoint(hosebirdEndpoint)
				.processor(new StringDelimitedProcessor(msgQueue));

		Client hosebirdClient = builder.build();
		return hosebirdClient;
	}
}
