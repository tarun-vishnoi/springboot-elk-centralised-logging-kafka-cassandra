package com.api.repo;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.api.pojo.Photo;

public interface PhotoCassandraRepo extends CassandraRepository<Photo, Integer> {

}
