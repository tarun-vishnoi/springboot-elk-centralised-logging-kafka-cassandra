package com.process.pojo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class Tweet implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKey
	private Integer id;
	private String tweetId;
	private String tweet;
	private Date createdDate;

	public Tweet() {
	}

	public Tweet(Integer id, String tweetId, String tweet, Date createdDate) {
		this.id = id;
		this.tweetId = tweetId;
		this.tweet = tweet;
		this.createdDate = createdDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTweetId() {
		return tweetId;
	}

	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}

	public String getTweet() {
		return tweet;
	}

	public void setTweet(String tweet) {
		this.tweet = tweet;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
