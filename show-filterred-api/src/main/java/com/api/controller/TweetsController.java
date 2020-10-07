package com.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.kafka.producer.TwitterProducer;

@RestController
@RequestMapping(path = "/twitter")
public class TweetsController {

	@Autowired
	private TwitterProducer twitterProducer;

	@GetMapping(path = "/save-live-tweets")
	public String saveTweets() {
		twitterProducer.produceTweets();
		return "Done";
	}

}
