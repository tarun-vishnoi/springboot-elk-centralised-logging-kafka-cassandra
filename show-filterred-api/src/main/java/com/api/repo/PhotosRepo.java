package com.api.repo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.api.doc.Photo;

public interface PhotosRepo extends ElasticsearchRepository<Photo, Integer> {

}
