package com.consumer.kafka;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.consumer.connectors.TwitterConnector;
import com.consumer.util.Constant;
import com.twitter.hbc.core.Client;

@Component
public class TweetsProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TweetsProducer.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private TwitterConnector twitterConnector;

	public void produceTweets() {
		/**
		 * Set up your blocking queues: Be sure to size these properly based on expected
		 * TPS of your stream
		 */
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
		Client client = twitterConnector.createTwitterClient(msgQueue);
		client.connect();

		// on a different thread, or multiple different threads....
		while (!client.isDone()) {
			String tweet = null;
			try {
				tweet = msgQueue.poll(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				client.stop();
			}

			if (tweet != null) {
				ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate
						.send(new ProducerRecord<String, String>(Constant.twitter_topic, tweet));
				listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

					@Override
					public void onSuccess(SendResult<String, String> result) {
						LOGGER.info("TwitterProducer :: produceTweets() :: onSuccess() :: topic = "
								+ result.getRecordMetadata().topic() + ", partition = "
								+ result.getRecordMetadata().partition() + ", Offset = "
								+ result.getRecordMetadata().offset());
					}

					@Override
					public void onFailure(Throwable ex) {
						LOGGER.error("TwitterProducer :: produceTweets() :: onFailure() :: ERROR" + ex.getMessage());
					}
				});
			}
		}
	}
}
