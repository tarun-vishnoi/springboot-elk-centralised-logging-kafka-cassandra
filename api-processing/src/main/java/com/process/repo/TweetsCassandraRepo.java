package com.process.repo;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.process.pojo.Tweet;

@Repository
public interface TweetsCassandraRepo extends CassandraRepository<Tweet, Integer> {

}
