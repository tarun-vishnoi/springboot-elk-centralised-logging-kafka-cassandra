package com.consumer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.consumer.kafka.TweetsProducer;

@RestController
@RequestMapping(path = "/twitter")
public class TweetsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TweetsController.class);

	@Autowired
	private TweetsProducer tweetsProducer;

	@GetMapping(path = "/save-live-tweets")
	public String saveTweets() {
		LOGGER.info("TweetsController :: saveTweets() :: Saving");
		tweetsProducer.produceTweets();
		return "Done";
	}
}
