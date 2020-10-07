package com.api.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.api.RO.PhotoRO;
import com.api.builder.PhotosBuilder;
import com.api.pojo.Photo;
import com.api.service.PhotosService;

@Component
public class PhotosConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(PhotosConsumer.class);

	@Autowired
	private PhotosService photosService;

	@Autowired
	private PhotosBuilder photosBuilder;

	@KafkaListener(topics = "topic_photos", groupId = "photos", containerFactory = "kafkaListenerContainerFactory4Photos")
	public void consume(PhotoRO photoRO) {
		Photo photo4Cassandra = photosBuilder.convert2CassandraEntity(photoRO);
		photosService.savePhoto2Cassandra(photo4Cassandra);
		LOGGER.info("PhotosConsumer ::  consume() :: Saved into Cassandra " + photo4Cassandra.getId());
		com.api.doc.Photo photo4ElasticDoc = photosBuilder.convert2ElasticDoc(photoRO);
		photosService.savePhotos2Elastic(photo4ElasticDoc);
		LOGGER.info("PhotosConsumer ::  consume() :: Saved into ElasticSearch " + photo4Cassandra.getId());
	}
}
