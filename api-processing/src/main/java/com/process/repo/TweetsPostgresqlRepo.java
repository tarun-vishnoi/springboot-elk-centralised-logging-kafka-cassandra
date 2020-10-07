package com.process.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.process.pojo.Tweets;

@Repository
public interface TweetsPostgresqlRepo extends JpaRepository<Tweets, Integer> {

}
